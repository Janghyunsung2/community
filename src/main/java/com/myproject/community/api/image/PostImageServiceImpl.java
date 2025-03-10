package com.myproject.community.api.image;

import com.myproject.community.domain.image.Image;
import com.myproject.community.domain.post.Post;
import com.myproject.community.domain.post.PostImage;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import com.myproject.community.infra.object_storage.ObjectStorage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PostImageServiceImpl implements PostImageService {


    private final ImageRepository imageRepository;
    private final PostImageRepository postImageRepository;
    private final ObjectStorage objectStorage;

    @Transactional
    public void savePostImages(Post post, List<MultipartFile> images) {
        for (MultipartFile image : images) {
            String imageUrl = objectStorage.imageUpload(image);
            Image newImage = Image.builder().path(imageUrl).build();
            imageRepository.save(newImage);
            PostImage postImage = PostImage.builder().post(post).image(newImage).build();
            postImageRepository.save(postImage);
        }
    }
}
