package com.myproject.community.api.comment.service;

import com.myproject.community.api.comment.dto.PostCommentRequestDto;
import com.myproject.community.api.comment.dto.PostCommentResponseDto;
import com.myproject.community.api.comment.dto.PostCommentResponseGroupDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostCommentService {
    void postCommentSave(long postId, PostCommentRequestDto postCommentRequestDto, HttpServletRequest request);

    List<PostCommentResponseGroupDto> getCommentGroupByPostId(long postId);

    void updatePostComment(long commentId, PostCommentRequestDto postCommentRequestDto);

    void deletePostCommenMember(long commentId);

    Page<PostCommentResponseDto> getCommentAll(Pageable pageable);

    void deletePostCommentAdmin(long commentId);

}
