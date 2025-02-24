package com.myproject.community.api.auth.service.auth.impl;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.auth.cookie.CookieUtil;
import com.myproject.community.api.auth.dto.MemberAuthDto;
import com.myproject.community.api.auth.dto.MemberAuthDto.MemberAccount;
import com.myproject.community.api.auth.dto.MemberLoginDto;
import com.myproject.community.api.auth.dto.PasswordRequestDto;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.auth.jwt.TokenInfo;
import com.myproject.community.api.auth.service.auth.AuthService;
import com.myproject.community.api.member.dto.MemberResponseDto;
import com.myproject.community.api.member.service.MemberService;
import com.myproject.community.domain.account.Account;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;


    @Override
    public TokenInfo authenticate(MemberLoginDto memberLoginDto, HttpServletResponse response) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(memberLoginDto.getUsername(),
                memberLoginDto.getPassword()));

        MemberAuthDto authMember = getAuthMember(memberService.getMemberIdByUsername(memberLoginDto.getUsername()));

        memberService.updateLastLogin(authMember.getUserId());

        TokenInfo tokenInfo = jwtProvider.generateToken(authMember);
        cookieUtil.setAuthCookies(tokenInfo, response);


        return tokenInfo;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean passwordCheckByMember(PasswordRequestDto passwordRequestDto,
        HttpServletRequest request) {

        long memberId = jwtProvider.getAuthUserId(request);
        Account account = accountRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Authentication authenticate = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(account.getUsername(), passwordRequestDto.getPassword()));

       return authenticate.isAuthenticated();
    }

    @Transactional(readOnly = true)
    public String getMemberUsername(long memberId) {
        return accountRepository.findById(memberId)
            .orElseThrow(()-> new CustomException(ErrorCode.MEMBER_NOT_FOUND))
            .getUsername();
    }


    public MemberAuthDto getAuthMember(long memberId) {

        Account account = accountRepository.findById(memberId)
            .orElseThrow(() -> new CustomException(ErrorCode.QUIT_ACCOUNT));
        MemberAccount memberAccount = new MemberAccount(account.getUsername(),
            account.getPassword());
        return new MemberAuthDto(memberId, memberAccount, account.getRole());
    }

    @Override
    public MemberResponseDto getMemberResponse(HttpServletRequest request) {
        return memberService.getMyPageMember(request);
    }


}
