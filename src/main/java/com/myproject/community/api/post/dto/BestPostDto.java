package com.myproject.community.api.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BestPostDto {

    private long id;
    private String title;
    private String imageUrl;

    @Builder
    @QueryProjection
    public BestPostDto(long id, String title, String imageUrl) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
    }
}
