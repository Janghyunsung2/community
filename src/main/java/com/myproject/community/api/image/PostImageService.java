package com.myproject.community.api.image;

import com.myproject.community.api.post.PostWithBoardDto;
import com.myproject.community.domain.post.Post;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PostImageService {
    void savePostImages(Post post, List<MultipartFile> images);
}
