package com.myproject.community.api.auth.jwt;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.auth.dto.MemberAuthDto;

import com.myproject.community.api.member.service.MemberService;
import com.myproject.community.domain.account.Account;
import com.myproject.community.domain.member.Member;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

    private final AccountRepository accountRepository;
    @Value("${jwt.secret.key}")
    private String secret;
    private final int accessExpirationTime = 1000 * 60 * 60;
    private final int refreshExpirationTime = 1000 * 60 * 60;
    private SecretKey key;
    private static final String JWT_KEY_PREFIX = "jwt:";
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberService memberService;
    private final UserDetailsService userDetailsService;

    public JwtProvider(RedisTemplate<String, String> redisTemplate, MemberService memberService,
        AccountRepository accountRepository, UserDetailsService userDetailsService) {
        this.redisTemplate = redisTemplate;
        this.memberService = memberService;
        this.accountRepository = accountRepository;
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    protected void init(){
        key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    public TokenInfo generateToken(MemberAuthDto dto) {
        String accessToken = generateAccessToken(dto);
        String refreshToken = generateRefreshToken(dto);

        redisTemplate.opsForValue().set(
            JWT_KEY_PREFIX + dto.getUserId(),
            refreshToken,
            refreshExpirationTime / 1000,  // ✅ 초 단위로 변환
            TimeUnit.SECONDS
        );

        return TokenInfo.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public TokenInfo validateToken(String reqRefreshToken) {
        Claims claims = parseClaims(reqRefreshToken);
        String refreshToken = redisTemplate.opsForValue().get(JWT_KEY_PREFIX + claims.getSubject());

        if(!refreshToken.equals(reqRefreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        long memberId = Long.parseLong(parseClaims(refreshToken).getSubject());
        MemberAuthDto authMember = memberService.getAuthMember(memberId);

        return generateToken(authMember);
    }

    public TokenInfo reissueToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        String refresh = redisTemplate.opsForValue().get(JWT_KEY_PREFIX + claims.getSubject());

        if(!refresh.equals(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        long memberId = Long.parseLong(parseClaims(refresh).getSubject());
        MemberAuthDto authMember = memberService.getAuthMember(memberId);

        return generateToken(authMember);
    }


    public void deleteRefreshToken(Long userId){
        Boolean deleted = redisTemplate.delete(JWT_KEY_PREFIX + userId);
        if (Boolean.FALSE.equals(deleted)) {
            log.warn("삭제할 리프레시 토큰이 없습니다. userId: {}", userId);  // ✅ 삭제할 토큰이 없을 경우 경고 로그 추가
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = parseClaims(token);
        String userId = claims.getSubject();
        Account account = accountRepository.findById(Long.parseLong(userId))
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        String username = account.getUsername();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public long getAuthUserId(HttpServletRequest request) {
        // claims.getSubject() 값이 userId
        String token = resolveTokenFromCookie(request);
        Claims claims = parseClaims(token);
        String userId = claims.getSubject();
        return Long.parseLong(userId);
    }

    private String resolveTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                if ("access-token".equals(cookie.getName())) {  // 🔹 JWT가 저장된 쿠키 이름 확인
                    return cookie.getValue();
                }
            }
        }
        log.warn("JWT 쿠키가 존재하지 않습니다.");
        throw new CustomException(ErrorCode.INVALID_TOKEN);
    }



    private String generateAccessToken(MemberAuthDto dto){
        Claims claims = null;
        try {
             claims = Jwts.claims().setSubject(String.valueOf(dto.getUserId()));
        }catch (Exception e){
            log.error(e.getMessage());
        }
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessExpirationTime);
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private String generateRefreshToken(MemberAuthDto dto){
        Claims claims = Jwts.claims().setSubject(String.valueOf(dto.getUserId()));
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshExpirationTime);
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.warn("만료된 JWT 토큰입니다: {}", e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);  // ✅ 예외 던지기 추가
        } catch (Exception e) {
            log.error("잘못된 JWT 토큰입니다: {}", e.getMessage());
            throw new CustomException(ErrorCode.INVALID_TOKEN);  // ✅ 예외 던지기 추가
        }
    }
}
