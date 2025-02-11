package com.myproject.community.api.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class MemberLoginDto {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
