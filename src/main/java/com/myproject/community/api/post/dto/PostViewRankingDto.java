package com.myproject.community.api.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostViewRankingDto {

    private long id;
    private String title;
    private long boardId;

    @QueryProjection
    @Builder
    public PostViewRankingDto(long id, String title, long boardId) {
        this.id = id;
        this.title = title;
        this.boardId = boardId;
    }


}
