package com.myproject.community.api.board.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardWithCategoryDto {

    private long id;
    private String title;
    private String description;
    private boolean active;

    private long categoryId;

    @Builder
    public BoardWithCategoryDto(long id, String title, String description, boolean active,
        long categoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.active = active;
        this.categoryId = categoryId;
    }


}
