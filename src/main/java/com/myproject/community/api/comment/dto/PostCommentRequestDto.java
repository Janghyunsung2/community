package com.myproject.community.api.comment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCommentRequestDto {

    @NotNull
    private String content;
    private long parentId;

}
