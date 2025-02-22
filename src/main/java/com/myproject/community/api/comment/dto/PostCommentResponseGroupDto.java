package com.myproject.community.api.comment.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCommentResponseGroupDto {

    private PostCommentResponseDto rootDto;
    private List<PostCommentResponseDto> childrenDtos;
}
