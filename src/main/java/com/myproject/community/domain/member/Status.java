package com.myproject.community.domain.member;

import lombok.Getter;

@Getter
public enum Status {

    MEMBER("회원"),
    WITHDRAWAL("탈퇴"),
    DORMANCY("휴먼");

    private final String description;

    Status(String description) {
        this.description = description;
    }

}
