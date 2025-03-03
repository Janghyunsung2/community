package com.myproject.community.api.auth.controller;

import com.myproject.community.api.auth.cookie.CookieUtil;
import com.myproject.community.api.auth.dto.MemberAuthDto;
import com.myproject.community.api.auth.dto.MemberLoginDto;
import com.myproject.community.api.auth.dto.PasswordRequestDto;
import com.myproject.community.api.auth.dto.VerifyEmailDto;
import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.auth.jwt.TokenInfo;
import com.myproject.community.api.auth.service.auth.AuthService;
import com.myproject.community.api.auth.service.email.EmailService;
import com.myproject.community.api.member.dto.MemberResponseDto;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final CookieUtil cookieUtil;
    private final JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated MemberLoginDto loginDto, HttpServletResponse response) {
        log.debug("로그인 요청데이터 ----- {}", loginDto);
        authService.authenticate(loginDto, response);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication, HttpServletRequest request) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        MemberResponseDto memberResponse = authService.getMemberResponse(request);
        // authentication.getPrinipal()에는 사용자의 상세 정보가 들어있음.
        return ResponseEntity.ok(memberResponse);
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        Cookie accessTokenCookie = cookieUtil.removeAccessTokenCookie();
        Cookie refreshTokenCookie = cookieUtil.removeRefreshTokenCookie();

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/send-email")
    public ResponseEntity<?> sendEmail(@RequestParam String email) throws MessagingException {

        emailService.sendVerificationEmail(email);
        return ResponseEntity.ok("인증 이메일이 발송되었습니다.");
    }

    @PostMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailDto verifyEmailDto){
        boolean isValid = emailService.verifyCode(verifyEmailDto.getEmail(), verifyEmailDto.getCode());

        if(!isValid){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 코드가 일치하지 않습니다.");

        }
        return ResponseEntity.ok("이메일 인증 완료!");
    }

    @PostMapping("/check-password")
    public ResponseEntity<?> checkPassword(@RequestBody PasswordRequestDto password, HttpServletRequest request){
        boolean check = authService.passwordCheckByMember(password, request);
        return ResponseEntity.ok(check);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = cookieUtil.getTokenFromCookie(request, "refresh_token").orElse(null);
        if(refreshToken != null){
            long memberId = jwtProvider.getAuthUserId(request);
            MemberAuthDto authMember = authService.getAuthMember(memberId);
            TokenInfo tokenInfo = jwtProvider.reissueToken(refreshToken, authMember);
            cookieUtil.setAuthCookies(tokenInfo, response);
            return ResponseEntity.ok(tokenInfo);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
    }


}
