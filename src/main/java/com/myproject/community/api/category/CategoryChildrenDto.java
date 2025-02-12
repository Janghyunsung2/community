package com.myproject.community.api.category;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryChildrenDto {

    private long id;
    @NotBlank
    private String name;
    private CategoryDto parent;

    @QueryProjection
    public CategoryChildrenDto(long id, String name, CategoryDto parent) {
        this.id = id;
        this.name = name;
        this.parent = parent;
    }
}
