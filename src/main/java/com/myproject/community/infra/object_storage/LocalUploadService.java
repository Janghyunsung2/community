package com.myproject.community.infra.object_storage;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("dev")
public class LocalUploadService implements ObjectStorage{

    @Override
    public String imageUpload(MultipartFile file) {
        return "";
    }
}
