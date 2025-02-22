package com.myproject.community.api.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberAdminResponseDto {

    private long id;
    private String username;
    private String nickname;
    private String email;
    private String phone;
    private String status;
    private String gender;
    private String role;

    @Builder
    @QueryProjection
    public MemberAdminResponseDto(long id, String username, String nickname, String email,
        String phone,
        String status, String gender, String role) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.gender = gender;
        this.role = role;
    }
}
