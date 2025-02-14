package com.myproject.community.api.image;

import com.myproject.community.domain.image.Image;
import com.myproject.community.domain.post.Post;
import com.myproject.community.domain.post.PostImage;
import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
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

    private final String UPLOAD_DIR = "src/main/resources/static/images/";

    private final ImageRepository imageRepository;
    private final PostImageRepository postImageRepository;

    @Transactional
    public void savePostImages(Post post, List<MultipartFile> images) {

        try {
            for (MultipartFile image : images) {
                String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get(UPLOAD_DIR + fileName);

                File dir = new File(UPLOAD_DIR);
                if(!dir.exists()){
                    dir.mkdirs();
                }

                image.transferTo(filePath.toFile());
                Image saveImage = Image.builder().path(fileName).build();

                imageRepository.save(saveImage);

                PostImage postImage = PostImage.builder()
                    .image(saveImage)
                    .post(post)
                    .build();

                postImageRepository.save(postImage);
            }
        }catch (IOException e){
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }
    }
}
