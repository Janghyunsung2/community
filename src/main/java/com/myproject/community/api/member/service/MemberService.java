package com.myproject.community.api.member.service;

import com.myproject.community.api.auth.dto.MemberAuthDto;
import com.myproject.community.api.member.dto.MemberCreateDto;
import com.myproject.community.api.member.dto.MemberUpdateDto;

public interface MemberService {

    void registerMember(MemberCreateDto dto);

    MemberAuthDto getAuthMember(long memberId);

    long getMemberIdByUsername(String loginId);

    void updateLastLogin(long memberId);

    void updateMember(MemberUpdateDto dto);

}
