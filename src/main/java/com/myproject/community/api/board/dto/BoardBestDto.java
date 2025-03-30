package com.myproject.community.api.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardBestDto {

    private long id;
    private String title;

    @Builder
    @QueryProjection
    public BoardBestDto(long id, String title) {
        this.id = id;
        this.title = title;
    }
}
