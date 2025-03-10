package com.myproject.community.infra.object_storage;

import org.springframework.web.multipart.MultipartFile;

public interface ObjectStorage {


    String imageUpload(MultipartFile file);


}
