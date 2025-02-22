package com.myproject.community.domain.comment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Comment parent;

    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private CommentStatus commentStatus;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Builder
    public Comment(String content, Comment parent) {
        this.content = content;
        this.parent = parent;
        this.createdAt = LocalDateTime.now();
        this.commentStatus = CommentStatus.ACTIVE;
    }

    public void addChild(Comment child) {
        children.add(child);
        child.parent = this;
    }

    public void updateComment(String content) {
        this.content = content;
    }

    public void isDeleted() {
        commentStatus = CommentStatus.AUTHOR_DELETED;
    }

    public void adminDelete() {
        commentStatus = CommentStatus.ADMIN_DELETED;
    }
}
