package com.myproject.community.api.category.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CategoryDto {


    @NotBlank
    private String name;
    private int displayOrder;

    @Builder
    @QueryProjection
    public CategoryDto(String name) {
        this.name = name;
    }

    public void updateDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    public void createRootCategory(String name){
        this.name = name;
    }
}
