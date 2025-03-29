package com.myproject.community.api.post.repository.querydsl;

import com.myproject.community.api.post.dto.PeriodType;
import com.myproject.community.api.post.dto.PostDetailDto;
import com.myproject.community.api.post.dto.PostListDto;
import com.myproject.community.api.post.dto.PostViewRankingDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<PostListDto> findPostsByBoardId(Long boardId, Pageable pageable);

    PostDetailDto findPostById(Long postId);


    List<PostViewRankingDto> findPostViewRankByDate(PeriodType periodType);
}
