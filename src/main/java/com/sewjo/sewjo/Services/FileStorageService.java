package com.sewjo.sewjo.Services;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileStorageService {

    public String uploadFile(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket("sewjo-4d3f7.appspot.com");
        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());
        String mediaLink = blob.getMediaLink();
        return mediaLink;
    }
}