package com.myproject.community.api.post.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostWithBoardDto {

    private String title;
    private String content;
    private List<MultipartFile> images;

    private long authorId;

    @Builder
    public PostWithBoardDto(String title, String content, List<MultipartFile> images, long authorId) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.images = images;
    }
}
