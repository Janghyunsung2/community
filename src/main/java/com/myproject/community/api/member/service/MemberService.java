package com.myproject.community.api.member.service;

import com.myproject.community.api.auth.dto.MemberAuthDto;
import com.myproject.community.api.member.dto.MemberRequestDto;

public interface MemberService {

    void registerMember(MemberRequestDto dto);

    MemberAuthDto getAuthMember(long memberId);

    long getMemberIdByUsername(String loginId);

    void updateLastLogin(long memberId);

}
