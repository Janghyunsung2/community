package com.myproject.community.domain.post;

import com.myproject.community.domain.board.Board;
import com.myproject.community.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title;

    private String content;

    private boolean isDeleted;

    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Post(String title, String content, Board board, Member member) {
        this.title = title;
        this.content = content;
        this.board = board;
        this.isDeleted = false;
        this.member = member;
        this.createdAt = LocalDateTime.now();
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

    public void delete(){
        this.isDeleted = true;
    }

}
