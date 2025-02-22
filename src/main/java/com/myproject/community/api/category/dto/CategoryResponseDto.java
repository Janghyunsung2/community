package com.myproject.community.api.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryResponseDto {

    private long id;

    private String name;

    @Builder
    @QueryProjection
    public CategoryResponseDto(long id, String name) {
        this.id = id;
        this.name = name;
    }
}
