package com.myproject.community.api.auth.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenInfo {

    private String grantType;
    private String accessToken;
    private String refreshToken;

}
