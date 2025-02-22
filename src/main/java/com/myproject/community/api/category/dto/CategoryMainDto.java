package com.myproject.community.api.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CategoryMainDto {

    private long categoryId;
    private String name;
    private int displayOrder;

    @QueryProjection
    public CategoryMainDto(long categoryId, String name, int displayOrder) {
        this.categoryId = categoryId;
        this.name = name;
        this.displayOrder = displayOrder;
    }
}
