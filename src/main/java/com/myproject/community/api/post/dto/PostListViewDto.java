package com.myproject.community.api.post.dto;

import com.myproject.community.api.board.dto.BoardDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListViewDto {

    private BoardInfoDto boardInfo;
    private Page<PostListDto> posts;

    @Builder
    public PostListViewDto(BoardInfoDto boardInfo, Page<PostListDto> posts) {
        this.boardInfo = boardInfo;
        this.posts = posts;
    }
}
