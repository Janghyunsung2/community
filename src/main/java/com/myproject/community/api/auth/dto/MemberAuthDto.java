package com.myproject.community.api.auth.dto;

import com.myproject.community.domain.account.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthDto {

    private long userId;

    private MemberAccount memberAccount;

    private Role role;

    @Getter
    @AllArgsConstructor
    public static class MemberAccount {
        private String id;
        private String password;
    }

}
