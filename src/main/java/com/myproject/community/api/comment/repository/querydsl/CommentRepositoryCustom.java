package com.myproject.community.api.comment.repository.querydsl;

import com.myproject.community.api.comment.dto.PostCommentResponseDto;
import com.myproject.community.api.comment.dto.PostCommentResponseGroupDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom{
    List<PostCommentResponseGroupDto> findCommentByPostId(Long postId);

    Page<PostCommentResponseDto> getPostCommentsAll(Pageable pageable);
}
