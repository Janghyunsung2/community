package com.myproject.community.api.member.repository.querydsl;

import com.myproject.community.api.member.dto.MemberAdminResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomMemberRepository {
    Page<MemberAdminResponseDto> getAdminPageMemberList(Pageable pageable);
}
