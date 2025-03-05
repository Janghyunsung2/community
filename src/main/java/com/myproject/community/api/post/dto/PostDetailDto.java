package com.myproject.community.api.post.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostDetailDto {

    private long id;
    private String title;
    private String content;
    private final List<String> url = new ArrayList<>();
    private String nickname;
    private boolean isDeleted;
    @Setter
    private long likeCount;
    @Setter
    private long viewCount;

    @QueryProjection
    public PostDetailDto(long id, String title, String content,  String nickname, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.isDeleted = isDeleted;
    }


    public void addUrlList(List<String> urlList) {
        if(urlList != null) {
            this.url.addAll(urlList);
        }
    }
}
