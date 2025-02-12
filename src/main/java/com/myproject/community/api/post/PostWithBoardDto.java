package com.myproject.community.api.post;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostWithBoardDto {

    private String title;
    private String content;

    private long authorId;
    private long boardId;

    @Builder
    public PostWithBoardDto(String title, String content, long authorId, long boardId) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.boardId = boardId;
    }
}
