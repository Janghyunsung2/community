package com.myproject.community.api.member.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberUpdateDto {

    private String name;
    private String nickname;
    private String password;
    private LocalDate birthday;
}
