package com.myproject.community.api.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    private String name;
    private String username;
    private String email;
    private String nickname;
    private String phone;
}
