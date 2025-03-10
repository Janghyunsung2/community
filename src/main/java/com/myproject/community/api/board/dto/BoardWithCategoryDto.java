package com.myproject.community.api.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardWithCategoryDto {

    private String title;
    private String description;
    private boolean active;

    private long categoryId;

    @Builder
    public BoardWithCategoryDto(String title, String description, boolean active,
        long categoryId) {

        this.title = title;
        this.description = description;
        this.active = active;
        this.categoryId = categoryId;
    }


}
