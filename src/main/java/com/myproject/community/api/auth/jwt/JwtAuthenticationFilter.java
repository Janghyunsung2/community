package com.myproject.community.api.auth.jwt;

import com.myproject.community.api.auth.cookie.CookieUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {

            String token = cookieUtil.getTokenFromCookie(request, "access-token").orElse(null);

            if (token == null) {
                filterChain.doFilter(request, response);
                return;
            }

            TokenInfo tokenInfo = jwtProvider.validateToken(token);
            if (tokenInfo != null) {
                Authentication authentication = jwtProvider.getAuthentication(
                    tokenInfo.getAccessToken());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
        }catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized");
        }

        filterChain.doFilter(request, response);

    }
}
