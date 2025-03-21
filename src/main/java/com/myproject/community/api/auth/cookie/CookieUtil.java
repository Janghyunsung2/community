package com.myproject.community.api.auth.cookie;

import com.myproject.community.api.auth.jwt.TokenInfo;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {

    private static final int REFRESH_TOKEN_MAX_AGE = 3 * 24 * 60 * 60; // 3일
    private static final int ACCESS_TOKEN_MAX_AGE = 3 * 24 * 60 * 60; //

    @Value("${spring.profiles.active}")
    private String profile;

    public void setAuthCookies(TokenInfo tokenInfo, HttpServletResponse response) {
        Cookie accessToken = getAccessTokenHttpSecureCookie(
            tokenInfo.getAccessToken());
        Cookie refreshToken = getRefreshTokenHttpSecureCookie(
            tokenInfo.getRefreshToken());

        response.addCookie(accessToken);
        response.addCookie(refreshToken);
    }

    public Cookie getRefreshTokenHttpSecureCookie(String refreshToken) {
        return createHttpSecureCookie("refresh-token", refreshToken, REFRESH_TOKEN_MAX_AGE);
    }

    public Cookie getAccessTokenHttpSecureCookie(String accessToken) {
        return createHttpSecureCookie("access-token", accessToken, ACCESS_TOKEN_MAX_AGE);
    }

    public Cookie removeRefreshTokenCookie(){
        return removeCookie("refresh-token");
    }
    public Cookie removeAccessTokenCookie(){
        return removeCookie("access-token");
    }

    private Cookie createHttpSecureCookie(String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true); // 클라이언트에서 Javascript로 쿠키 접근 불가
        cookie.setSecure(!profile.equals("dev")); // 'dev' 프로파일이 아닌 경우 Secure 활성화
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");

        return cookie;
    }

    private Cookie removeCookie(String name){
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(!profile.equals("dev"));
        cookie.setPath("/");
        cookie.setMaxAge(0);  // 만료된 쿠키로 설정

        return cookie;
    }

    public Optional<String> getTokenFromCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() != null) {
            return Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst();
        }
        return Optional.empty();
    }
}
