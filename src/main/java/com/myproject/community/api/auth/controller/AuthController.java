package com.myproject.community.api.auth.controller;

import com.myproject.community.api.auth.cookie.CookieUtil;
import com.myproject.community.api.auth.dto.MemberLoginDto;
import com.myproject.community.api.auth.dto.VerifyEmailDto;
import com.myproject.community.api.auth.service.auth.AuthService;
import com.myproject.community.api.auth.service.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailService emailService;
    private final CookieUtil cookieUtil;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Validated @RequestBody MemberLoginDto loginDto, HttpServletResponse response) {

        authService.authenticate(loginDto, response);

        return ResponseEntity.ok().build();
    }

//    @PostMapping("/check")
//    public ResponseEntity<Void> loginCheck(HttpServletRequest request) {
//        cookieUtil.getAccessTokenHttpSecureCookie()
//        return ResponseEntity.ok().build();
//    }


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


}
