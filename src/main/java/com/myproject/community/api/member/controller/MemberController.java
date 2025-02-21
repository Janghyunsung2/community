package com.myproject.community.api.member.controller;

import com.myproject.community.api.auth.jwt.JwtProvider;
import com.myproject.community.api.member.dto.MemberCreateDto;
import com.myproject.community.api.member.dto.MemberResponseDto;
import com.myproject.community.api.member.dto.MemberUpdateDto;
import com.myproject.community.api.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping("/register")
    public ResponseEntity<String> register(@Validated @RequestBody MemberCreateDto dto){
        memberService.registerMember(dto);
        return ResponseEntity.ok("Member registered successfully");
    }

    @PutMapping
    public ResponseEntity<String> update(@Validated @RequestBody MemberUpdateDto dto, HttpServletRequest request){
        memberService.updateMember(dto, request);
        return ResponseEntity.ok("Member updated successfully");
    }

    @GetMapping
    public ResponseEntity<MemberResponseDto> getMember(HttpServletRequest request){
        MemberResponseDto myPageMember = memberService.getMyPageMember(request);
        return ResponseEntity.ok(myPageMember);
    }


    @GetMapping("/check-nickname")
    public ResponseEntity<Boolean> checkNickname(@RequestParam String nickname){
        return ResponseEntity.ok(memberService.isNickNameExist(nickname));
    }


    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username){
        return ResponseEntity.ok(memberService.isUserNameExist(username));
    }


}
