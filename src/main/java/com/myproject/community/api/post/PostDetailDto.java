package com.myproject.community.api.post;

import com.querydsl.core.annotations.QueryProjection;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostDetailDto {

    private long id;
    private String title;
    private String content;
    private List<String> url;
    private String nickname;

    @QueryProjection
    public PostDetailDto(long id, String title, String content,  String nickname) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
    }


    public void addUrlList(List<String> urlList) {
        this.url.addAll(urlList);
    }
}
