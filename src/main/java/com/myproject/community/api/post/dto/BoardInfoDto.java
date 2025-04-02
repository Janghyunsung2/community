package com.myproject.community.api.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardInfoDto {
    private long id;
    private String title;

    @QueryProjection
    @Builder
    public BoardInfoDto(long id, String title) {
        this.id = id;
        this.title = title;
    }
}
