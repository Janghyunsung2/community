package com.myproject.community.api.auth.jwt;

import com.myproject.community.api.auth.dto.MemberAuthDto;

import com.myproject.community.api.member.service.MemberService;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtProvider {

    @Value("${jwt.secret.key}")
    private String secret;
    private final int accessExpirationTime = 1000 * 60 * 60;
    private final int refreshExpirationTime = 1000 * 60 * 60;
    private SecretKey key;
    private static final String JWT_KEY_PREFIX = "jwt:";
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberService memberService;

    public JwtProvider(RedisTemplate<String, String> redisTemplate, MemberService memberService) {
        this.redisTemplate = redisTemplate;
        this.memberService = memberService;
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
            refreshExpirationTime,
            TimeUnit.MILLISECONDS
        );

        return TokenInfo.builder()
            .grantType("Bearer")
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    public TokenInfo validateToken(String reqRefreshToken) {
        Claims claims = parseClaims(reqRefreshToken);
        String refreshToken = redisTemplate.opsForValue().get(JWT_KEY_PREFIX + claims.getSubject()).toString();

        if(!refreshToken.equals(reqRefreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        long memberId = Long.parseLong(parseClaims(refreshToken).getSubject());
        MemberAuthDto authMember = memberService.getAuthMember(memberId);

        return generateToken(authMember);
    }

    public TokenInfo reissueToken(String refreshToken) {
        Claims claims = parseClaims(refreshToken);
        String refresh = redisTemplate.opsForValue().get(JWT_KEY_PREFIX + claims.getSubject()).toString();

        if(!refresh.equals(refreshToken)){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        long memberId = Long.parseLong(parseClaims(refresh).getSubject());
        MemberAuthDto authMember = memberService.getAuthMember(memberId);

        return generateToken(authMember);
    }


    public void deleteRefreshToken(Long userId){
        redisTemplate.delete(JWT_KEY_PREFIX + userId);
    }

    private String generateAccessToken(MemberAuthDto dto){
        Claims claims = Jwts.claims().setSubject(String.valueOf(dto.getUserId()));
        Date now = new Date();
        Date validity = new Date(now.getTime() + accessExpirationTime);
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(key, SignatureAlgorithm.ES256)
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
            .signWith(key, SignatureAlgorithm.ES256)
            .compact();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());
            return e.getClaims();
        }
    }
}
