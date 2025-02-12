package com.myproject.community.api.category;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryDto {

    @NotBlank
    private long id;
    @NotBlank
    private String name;

    @QueryProjection
    public CategoryDto(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void createRootCategory(String name){
        this.name = name;
    }
}
