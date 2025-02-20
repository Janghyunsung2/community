package com.myproject.community.api.like.post.service;

import com.myproject.community.api.like.LikeCountResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface PostLikeService {


    void savePostLike(long postId, HttpServletRequest request);
    void deletePostLike(long postId, HttpServletRequest request);

    LikeCountResponseDto getPostLikeCount(long postId);

    boolean isPostLiked(long postId, HttpServletRequest request);
}
