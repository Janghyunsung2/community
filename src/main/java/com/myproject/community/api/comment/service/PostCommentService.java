package com.myproject.community.api.comment.service;

import com.myproject.community.api.comment.PostCommentRequestDto;
import com.myproject.community.api.comment.PostCommentResponseGroupDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostCommentService {
    void postCommentSave(long postId, PostCommentRequestDto postCommentRequestDto, HttpServletRequest request);

    List<PostCommentResponseGroupDto> getCommentGroupByPostId(long postId);

    void updatePostComment(long commentId, PostCommentRequestDto postCommentRequestDto);

    void deletePostComment(long commentId);

}
