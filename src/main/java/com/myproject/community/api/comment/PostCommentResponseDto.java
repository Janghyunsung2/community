package com.myproject.community.api.comment;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostCommentResponseDto {

    private long contentId;
    private String content;
    private LocalDateTime createdAt;
    private long contentLikes;
    private String nickName;

    @QueryProjection
    public PostCommentResponseDto(long contentId, String content, LocalDateTime createdAt
      , String nickName) {
        this.contentId = contentId;
        this.content = content;
        this.createdAt = createdAt;
        this.nickName = nickName;
    }

    public void setContentLikes(long contentLikes) {
        this.contentLikes = contentLikes;
    }
}
