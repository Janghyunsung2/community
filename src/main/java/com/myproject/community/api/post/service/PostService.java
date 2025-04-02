package com.myproject.community.api.post.service;

import com.myproject.community.api.post.dto.BestPostDto;
import com.myproject.community.api.post.dto.PeriodType;
import com.myproject.community.api.post.dto.PostListViewDto;
import com.myproject.community.api.post.dto.PostUpdateDto;
import com.myproject.community.api.post.dto.PostViewRankingDto;
import com.myproject.community.api.post.dto.PostWithBoardDto;
import com.myproject.community.api.post.dto.PostDetailDto;
import com.myproject.community.api.post.dto.PostListDto;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    void createPost(long boardId, PostWithBoardDto postWithBoardDto, HttpServletRequest request);

    PostListViewDto getPosts(long boardId, Pageable pageable);

    PostDetailDto getPostDetail(long postId, HttpServletRequest request);

    void updatePost(long postId, PostUpdateDto postUpdateDto, HttpServletRequest request);

    void deletePost(long postId, HttpServletRequest request);
    void deleteAdminPost(long postId, HttpServletRequest request);

    Page<PostListDto> getPostsByKeyword(String keyword, Pageable pageable);

    void viewCount(long postId);

    List<PostViewRankingDto> getPostViewRanking(PeriodType period);

    List<BestPostDto> getBestPosts(Long boardId);
}
