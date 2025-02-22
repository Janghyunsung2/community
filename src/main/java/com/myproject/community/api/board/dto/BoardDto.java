package com.myproject.community.api.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardDto {


    private long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;

    private boolean active;

    @QueryProjection
    @Builder
    public BoardDto(long id, String title, String description, boolean active) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.active = active;
    }


}
