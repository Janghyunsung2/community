package com.myproject.community.api.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListDto {

    private long postId;
    private String title;
    private String nickName;
    private LocalDateTime createAt;
    private long views;

    @Builder
    @QueryProjection
    public PostListDto(long postId, String title, String nickName, LocalDateTime createAt) {
        this.postId = postId;
        this.title = title;
        this.nickName = nickName;
        this.createAt = createAt;
    }

    public void setViews(long views) {
        this.views = views;
    }


}
