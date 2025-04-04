package com.myproject.community.api.image;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.myproject.community.domain.image.Image;
import com.myproject.community.domain.post.Post;
import com.myproject.community.domain.post.PostImage;
import com.myproject.community.infra.object_storage.S3Service;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class PostImageServiceImplTest {

    @Mock
    ImageRepository imageRepository;
    @Mock
    PostImageRepository postImageRepository;
    @Mock
    S3Service s3Service;
    @InjectMocks
    PostImageServiceImpl postImageService;

    @Test
    @DisplayName("게시물 이미지 저장")
    void postImageCreateTest(){
        Post post = mock(Post.class);
        List<MultipartFile> files = List.of(mock(MultipartFile.class));



        postImageService.savePostImages(post, files);

        verify(postImageRepository).save(any(PostImage.class));
        verify(imageRepository).save(any(Image.class));

    }

}