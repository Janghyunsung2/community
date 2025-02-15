package com.myproject.community.api.post;

import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostListDto {

    private long postId;
    private String title;
    private String nickName;
    private LocalDateTime createAt;

    @QueryProjection
    public PostListDto(long postId, String title, String nickName, LocalDateTime createAt) {
        this.postId = postId;
        this.title = title;
        this.nickName = nickName;
        this.createAt = createAt;
    }
}
