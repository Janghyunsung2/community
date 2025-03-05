package com.myproject.community.api.post.repository.querydsl;

import com.myproject.community.api.post.dto.PostDetailDto;
import com.myproject.community.api.post.dto.PostListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostListDto> findPostsByBoardId(Long boardId, Pageable pageable);

    PostDetailDto findPostById(Long postId);
}
