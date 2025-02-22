package com.myproject.community.domain.member;

import lombok.Getter;

@Getter
public enum Status {

    ACTIVE("활성"),
    WITHDRAWAL("탈퇴"),
    DORMANCY("휴먼"),
    PERMANENT_BAN("영구정지");

    private final String description;

    Status(String description) {
        this.description = description;
    }

}
