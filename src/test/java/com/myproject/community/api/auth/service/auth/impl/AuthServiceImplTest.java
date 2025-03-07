package com.myproject.community.api.auth.service.auth.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import com.myproject.community.api.account.AccountRepository;
import com.myproject.community.api.auth.cookie.CookieUtil;
import com.myproject.community.api.auth.dto.MemberAuthDto;
import com.myproject.community.api.auth.dto.MemberAuthDto.MemberAccount;
import com.myproject.community.api.auth.dto.MemberLoginDto;
import com.myproject.community.api.auth.dto.PasswordRequestDto;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.auth.jwt.TokenInfo;
import com.myproject.community.api.member.dto.MemberResponseDto;
import com.myproject.community.api.member.service.MemberService;
import com.myproject.community.domain.account.Account;
import com.myproject.community.domain.account.Role;
import com.myproject.community.domain.member.Member;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private MemberService memberService;
    @Mock
    private JwtProvider jwtProvider;
    @Mock
    private CookieUtil cookieUtil;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private AccountRepository accountRepository;
    @InjectMocks
    private AuthServiceImpl authServiceImpl;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Test
    @DisplayName("로그인 인증성공")
    void authenticate() {
        String username = "username";
        String password = "password";
        long memberId = 1L;
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            username, password);
        MemberLoginDto memberLoginDto = MemberLoginDto.builder().username(username).password(password)
            .build();

        Member member = Member.builder().build();

        Mockito.when(authenticationManager.authenticate(authenticationToken))
            .thenReturn(authenticationToken);
        TokenInfo tokenInfo = TokenInfo.builder()
            .accessToken("access")
            .refreshToken("refresh")
            .grantType("bearer")
            .build();

        Mockito.when(memberService.getMemberIdByUsername(memberLoginDto.getUsername())).thenReturn(memberId);
        Mockito.when(accountRepository.findById(memberId)).thenReturn(
            Optional.ofNullable(Account.builder().username(username).password(password).member(member).role(Role.MEMBER).build()));
        Mockito.when(memberService.getMemberIdByUsername(username)).thenReturn(memberId);

        authServiceImpl.authenticate(memberLoginDto, response);

        Mockito.verify(memberService, Mockito.times(1)).updateLastLogin(memberId);
        Mockito.verify(memberService, Mockito.times(1)).getMemberIdByUsername(username);

    }

    @Test
    @DisplayName("비밀번호 검증성공")
    void passwordCheckSuccess() {
        long memberId = 1L;
        String password = "1234";
        PasswordRequestDto passwordRequestDto = new PasswordRequestDto(password);
        Member member = Member.builder().build();
        Account account = Account.builder().username("test").password("1234").role(Role.MEMBER).member(member).build();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
            account.getUsername(), password, Collections.singletonList(new SimpleGrantedAuthority("ROLE_MEMBER")));

        Mockito.when(jwtProvider.getAuthUserId(request)).thenReturn(memberId);
        Mockito.when(accountRepository.findById(memberId)).thenReturn(Optional.of(account));
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authenticationToken);

        boolean authentication = authServiceImpl.passwordCheckByMember(passwordRequestDto, request);

        assertTrue(authentication);
    }

    @Test
    @DisplayName("비밀번호 검증실패")
    void passwordCheckFailure() {
        long memberId = 1L;
        String password = "4856";
        PasswordRequestDto passwordRequestDto = new PasswordRequestDto(password);
        Member member = Member.builder().build();
        Account account = Account.builder().username("test").password("1234").role(Role.MEMBER).member(member).build();

        Mockito.when(jwtProvider.getAuthUserId(request)).thenReturn(memberId);
        Mockito.when(accountRepository.findById(memberId)).thenReturn(Optional.of(account));
        Mockito.when(authenticationManager.authenticate(any()))
            .thenReturn(new UsernamePasswordAuthenticationToken(account.getPassword(), password));

        boolean authentication = authServiceImpl.passwordCheckByMember(passwordRequestDto, request);

        assertFalse(authentication);
    }

    @Test
    @DisplayName("유저네임조회성공")
    void getMemberUsernameSuccess() {
        long memberId = 1L;
        String username = "username";
        Member member = Member.builder().build();
        Account account = Account.builder().username(username).role(Role.MEMBER).member(member).build();
        Mockito.when(accountRepository.findById(memberId)).thenReturn(Optional.of(account));

        String memberUsername = authServiceImpl.getMemberUsername(memberId);

        Assertions.assertEquals(username, memberUsername);
    }

    @Test
    @DisplayName("유저네임조회실패")
    void getMemberUsernameFailure() {
        long memberId = 1L;
        Mockito.when(accountRepository.findById(memberId)).thenThrow(CustomException.class);

        assertThrows(CustomException.class, () -> authServiceImpl.getMemberUsername(memberId));
    }

    @Test
    @DisplayName("맴버 조회")
    void getMemberSuccess() {
        MemberResponseDto memberResponseDto = MemberResponseDto.builder().name("test").build();
        Mockito.when(memberService.getMyPageMember(request)).thenReturn(memberResponseDto);

        MemberResponseDto memberResponse = authServiceImpl.getMemberResponse(request);

        Assertions.assertEquals(memberResponseDto, memberResponse);
    }
}