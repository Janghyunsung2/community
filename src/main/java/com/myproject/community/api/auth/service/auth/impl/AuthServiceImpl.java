package com.myproject.community.api.auth.service.auth.impl;

import com.myproject.community.api.auth.cookie.CookieUtil;
import com.myproject.community.api.auth.dto.MemberAuthDto;
import com.myproject.community.api.auth.dto.MemberLoginDto;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.auth.jwt.TokenInfo;
import com.myproject.community.api.auth.service.auth.AuthService;
import com.myproject.community.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final AuthenticationManager authenticationManager;


    @Override
    public TokenInfo authenticate(MemberLoginDto memberLoginDto, HttpServletResponse response) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(memberLoginDto.getUsername(),
                memberLoginDto.getPassword()));

        MemberAuthDto authMember = memberService.getAuthMember(
            memberService.getMemberIdByUsername(memberLoginDto.getUsername()));

        memberService.updateLastLogin(authMember.getUserId());

        TokenInfo tokenInfo = jwtProvider.generateToken(authMember);
        cookieUtil.setAuthCookies(tokenInfo, response);


        return tokenInfo;
    }




}
