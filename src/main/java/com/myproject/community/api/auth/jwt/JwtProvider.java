package com.myproject.community.api.auth.jwt;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.auth.dto.MemberAuthDto;


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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final AccountRepository accountRepository;
    @Value("${jwt.secret.key}")
    private String secret;
    private final int accessExpirationTime = 1000 * 60 * 60;
    private final int refreshExpirationTime = 1000 * 60 * 60;
    private SecretKey key;
    private static final String JWT_KEY_PREFIX = "jwt:";
    private final RedisTemplate<String, String> redisTemplate;



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

    public TokenInfo validateToken(String reqRefreshToken, MemberAuthDto authMember) {
        Claims claims = parseClaims(reqRefreshToken);
        String refreshToken = redisTemplate.opsForValue().get(JWT_KEY_PREFIX + claims.getSubject());

        if(!refreshToken.equals(reqRefreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return generateToken(authMember);
    }

    public TokenInfo reissueToken(String refreshToken, MemberAuthDto authMember) {
        Claims claims = parseClaims(refreshToken);
        String refresh = redisTemplate.opsForValue().get(JWT_KEY_PREFIX + claims.getSubject());

        if(!refresh.equals(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        return generateToken(authMember);
    }


    public void deleteRefreshToken(Long userId){
        Boolean deleted = redisTemplate.delete(JWT_KEY_PREFIX + userId);
        if (Boolean.FALSE.equals(deleted)) {
            log.warn("삭제할 리프레시 토큰이 없습니다. userId: {}", userId);  // ✅ 삭제할 토큰이 없을 경우 경고 로그 추가
        }
    }
//
//    public Authentication getAuthentication(String token) {
//        Claims claims = parseClaims(token);
//        String userId = claims.getSubject();
//
//        return new UsernamePasswordAuthenticationToken(userId, "", new ArrayList<>());
//    }

    public long getAuthUserId(HttpServletRequest request) {
        // claims.getSubject() 값이 userId
        String token = resolveAccessTokenFromCookie(request);
        Claims claims = parseClaims(token);
        String userId = claims.getSubject();
        return Long.parseLong(userId);
    }

    public long getMemberIdByRefreshToken(HttpServletRequest request) {
        String refreshToken = resolveRefreshTokenFromCookie(request);
        Claims claims = parseClaims(refreshToken);
        String userId = claims.getSubject();
        return Long.parseLong(userId);
    }



    private String resolveAccessTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("access-token".equals(cookie.getName())) {  // 🔹 JWT가 저장된 쿠키 이름 확인
                    return cookie.getValue();
                }
            }
        }
        log.warn("JWT 쿠키가 존재하지 않습니다.");
        return null;
    }

    private String resolveRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refresh-token".equals(cookie.getName())) {  // 🔹 JWT가 저장된 쿠키 이름 확인
                    return cookie.getValue();
                }
            }
        }
        log.warn("JWT 쿠키가 존재하지 않습니다.");
        return null;
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
