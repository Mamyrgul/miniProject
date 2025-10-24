package com.example.miniproject.controller;

import com.example.miniproject.service.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Tag(name = "File Upload", description = "Работа с загрузкой изображений через AWS S3")
@CrossOrigin(origins ="*", maxAge = 3600)
@RestController
@RequestMapping("/api/s3")
@RequiredArgsConstructor
public class S3Api {
    private final S3Service s3Service;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Добавление файла в S3", description = "Метод для загрузки файла в S3")
    public List<String> uploadFiles(@RequestParam("files") List<MultipartFile> files) throws IOException {
        return s3Service.uploadFiles(files);
    }

    @DeleteMapping("/delete-pdf")
    @Operation(summary = "Удалить PDF-файлы из S3", description = "Удалить PDF-файлы в S3")
    public String deletePdf(@RequestParam String fileRef) {
        return s3Service.deletedFile(fileRef);
    }
}
