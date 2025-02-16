package com.myproject.community.api.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    void createPost(long boardId, PostWithBoardDto postWithBoardDto);

    Page<PostListDto> getPosts(long boardId, Pageable pageable);

    PostDetailDto getPostDetail(long postId);

    void updatePost(PostUpdateDto postUpdateDto);

    void deletePost(long postId);
}
