package com.myproject.community.api.post.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@Builder
@Getter
public class PostUpdateDto {

    private long postId;
    private String title;
    private String content;
    private List<MultipartFile> images;

}
