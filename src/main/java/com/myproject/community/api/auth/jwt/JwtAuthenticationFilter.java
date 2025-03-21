package com.myproject.community.api.auth.jwt;


import com.myproject.community.api.auth.cookie.CookieUtil;
import com.myproject.community.api.auth.dto.MemberAuthDto;
import com.myproject.community.api.auth.service.auth.AuthService;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;


@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final UserDetailsService userDetailsService;
    private final AuthService authService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = cookieUtil.getTokenFromCookie(request, "access-token").orElse(null);
            if (accessToken == null) {
                filterChain.doFilter(request, response);
                return;
            }

            if (!jwtProvider.validateToken(accessToken)) {
                refreshToken(request, response, filterChain);
                return;
            }

            long userId = jwtProvider.getMemberIdByToken(request);
            String memberUsername = authService.getMemberUsername(userId);

            UserDetails userDetails = userDetailsService.loadUserByUsername(memberUsername);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (JwtException e) {
            logger.error("JWT 오류", e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid JWT Token");

            return;
        } catch (Exception e) {
            logger.error("알 수 없는 오류", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error");
            return;
        }

        filterChain.doFilter(request, response);


    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return "OPTIONS".equalsIgnoreCase(request.getMethod());
    }

    private void refreshToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        try {
            String refreshToken = cookieUtil.getTokenFromCookie(request,
                "refresh-token").orElse(null);
            if (refreshToken == null) {
                filterChain.doFilter(request, response);
                return;
            }
            MemberAuthDto authMember = authService.getAuthMember(
                jwtProvider.getMemberIdByRefreshToken(request));
            log.debug("리프레쉬토큰을 이용해 재발급되었습니다 userId:{}", authMember.getUserId());
            TokenInfo tokenInfo = jwtProvider.reissueToken(refreshToken, authMember);
            if (tokenInfo == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized");
            } else {
                cookieUtil.setAuthCookies(tokenInfo, response);
                UserDetails userDetails = userDetailsService.loadUserByUsername(
                    authMember.getMemberAccount().getId());
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);

                filterChain.doFilter(request, response);
            }
        }catch (Exception e) {
            filterChain.doFilter(request, response);
        }




    }
}
