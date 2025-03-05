package com.myproject.community.api.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardAdminDto {
    private long id;
    private String title;
    private String description;
    private boolean active;
    private String categoryName;

    @QueryProjection
    public BoardAdminDto(long id, String title, String description, boolean active,
        String categoryName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.active = active;
        this.categoryName = categoryName;
    }
}
