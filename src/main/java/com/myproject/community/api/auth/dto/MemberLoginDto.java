package com.myproject.community.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLoginDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
