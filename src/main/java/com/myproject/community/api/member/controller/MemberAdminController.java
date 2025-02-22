package com.myproject.community.api.member.controller;

import com.myproject.community.api.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/members")
@RequiredArgsConstructor
public class MemberAdminController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<?> getAdminPageMembers(Pageable pageable) {
        return ResponseEntity.ok(memberService.getMembersByAdmin(pageable));
    }
}
