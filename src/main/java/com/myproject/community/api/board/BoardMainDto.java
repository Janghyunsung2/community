package com.myproject.community.api.board;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class BoardMainDto {

    private long boardId;
    private String title;
    private long categoryId;

    @QueryProjection
    public BoardMainDto(long boardId, String title, long categoryId) {
        this.boardId = boardId;
        this.title = title;
        this.categoryId = categoryId;
    }
}
