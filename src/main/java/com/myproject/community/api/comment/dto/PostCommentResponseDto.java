package com.myproject.community.api.comment.dto;

import com.myproject.community.domain.comment.CommentStatus;
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
    private CommentStatus status;

    @QueryProjection
    public PostCommentResponseDto(long contentId, String content, LocalDateTime createdAt
      , String nickName, CommentStatus status) {
        this.contentId = contentId;
        this.content = content;
        this.createdAt = createdAt;
        this.nickName = nickName;
        this.status = status;
    }

    public void setContentLikes(long contentLikes) {
        this.contentLikes = contentLikes;
    }
}
