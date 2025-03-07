package com.myproject.community.api.post.dto;

import java.util.List;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class PostUpdateDto {

    private long postId;
    private String title;
    private String content;
    private List<MultipartFile> images;
}
