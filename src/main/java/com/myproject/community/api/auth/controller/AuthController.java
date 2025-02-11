package com.myproject.community.api.auth.controller;

import com.myproject.community.api.auth.dto.MemberLoginDto;
import com.myproject.community.api.auth.service.auth.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Validated @RequestBody MemberLoginDto loginDto, HttpServletResponse response) {

        authService.authenticate(loginDto, response);

        return ResponseEntity.ok().build();
    }

}
