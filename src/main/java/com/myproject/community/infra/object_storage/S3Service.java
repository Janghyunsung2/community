package com.myproject.community.infra.object_storage;

import com.myproject.community.error.CustomException;
import com.myproject.community.error.ErrorCode;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
@RequiredArgsConstructor
public class S3Service implements ObjectStorage{

    private final S3Client s3Client;

    @Value("${S3.bucket.name}")
    private String bucketName;

    private final String region = "us-east-2";


    @Override
    public String imageUpload(MultipartFile file) {
        String keyName = "uploads/" + file.getOriginalFilename();

        try(InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType(file.getContentType())
                .build(),
                RequestBody.fromInputStream(inputStream, file.getSize())
                );
        }catch (Exception e) {
            throw new CustomException(ErrorCode.IMAGE_NOT_FOUND);
        }
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + keyName;
    }
}
