package com.example.miniproject.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3Service {
    List<String> uploadFiles(List<MultipartFile> files) throws IOException;
    String updateFile(String fileFromBucket, MultipartFile newFile) throws IOException;
    List<String> deleteFiles(List<String> fileRefs);
    String addFile(MultipartFile file) throws IOException;
    String deletedFile(String fileRef);
}

