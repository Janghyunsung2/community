package com.myproject.community.api.comment.querydsl;

import com.myproject.community.api.comment.PostCommentResponseGroupDto;
import java.util.List;

public interface CommentRepositoryCustom{
    List<PostCommentResponseGroupDto> findCommentByPostId(Long postId);
}
