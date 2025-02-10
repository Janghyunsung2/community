package com.myproject.community.api.member.controller;

import com.myproject.community.api.member.dto.MemberRequestDto;
import com.myproject.community.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<String> register(@Validated @RequestBody MemberRequestDto dto){
        memberService.registerMember(dto);
        return ResponseEntity.ok("Member registered successfully");
    }
}
