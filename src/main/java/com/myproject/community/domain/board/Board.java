package com.myproject.community.domain.board;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String description;

    private boolean active;

    @Builder
    public Board(String title, String description, boolean active) {
        this.title = title;
        this.description = description;
        this.active = active;
    }

    public void update(String title, String description, boolean active) {
        this.title = title;
        this.description = description;
        this.active = active;
    }
}
