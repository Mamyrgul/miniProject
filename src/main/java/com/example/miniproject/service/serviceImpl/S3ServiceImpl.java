package com.example.miniproject.service.serviceImpl;

import com.example.miniproject.service.S3Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3ServiceImpl implements S3Service {

    @Value("${aws.bucket-name}")
    private String bucketName;
    private final S3Client s3Client;
    private final String region;

    public S3ServiceImpl(S3Client s3Client,
                         @Value("${aws.bucket-name}") String bucketName,
                         @Qualifier("s3Region") String region) {
        this.s3Client = s3Client;
        this.bucketName = bucketName;
        this.region = region;
    }

    @Override
    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> responses = new ArrayList<>();
        for (MultipartFile file : files) {
            responses.add(uploadFile(file));
        }
        return responses;
    }

    private String uploadFile(MultipartFile multipartFile) throws IOException {
        String originalFileName = multipartFile.getOriginalFilename();
        String cleanedFileName = cleanFileName(originalFileName);
        String fileName = System.currentTimeMillis() + "_" + cleanedFileName;

        File file = convertMultiPartToFile(multipartFile, cleanedFileName);

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .build();

        s3Client.putObject(putRequest, RequestBody.fromFile(file));

        file.delete();

        return generateS3Url(fileName);
    }

    private File convertMultiPartToFile(MultipartFile multipartFile, String cleanedFileName) throws IOException {
        File convFile = new File(cleanedFileName);
        try (FileOutputStream fos = new FileOutputStream(convFile)) {
            fos.write(multipartFile.getBytes());
        }
        return convFile;
    }

    private String cleanFileName(String originalFilename) {
        if (originalFilename == null) {
            return "file";
        }
        return originalFilename.replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
    }


    public String updateFile(String fileFromBucket, MultipartFile newFile) throws IOException {
        if (fileFromBucket == null || fileFromBucket.trim().isEmpty()) {
            return "Ошибка: ссылка на файл в бакете не указана";
        }

        String deleteResult = deleteFile(fileFromBucket);
        if (deleteResult.startsWith("Ошибка")) {
            return deleteResult;
        }

        String uploadResult;
        try {
            uploadResult = uploadFile(newFile);
        } catch (Exception e) {
            return "Ошибка при загрузке нового файла: " + e.getMessage();
        }

        return String.format("Файл удалён: %s. Заменён на: %s", deleteResult, uploadResult);
    }

    @Override
    public List<String> deleteFiles(List<String> fileRefs) {
        List<String> responses = new ArrayList<>();
        for (String ref : fileRefs) {
            responses.add(deleteFile(ref));
        }
        return responses;
    }

    @Override
    public String addFile(MultipartFile file) throws IOException {
        return uploadFile(file);
    }

    @Override
    public String deletedFile(String fileRef) {
        String fileUrl = fileRef.trim();
        return deleteFile(fileUrl);
    }

    private String deleteFile(String fileRef) {
        String key = extractKeyFromUrl(fileRef);
        try {
            // Файл бар экенин текшерүү
            HeadObjectRequest headRequest = HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.headObject(headRequest);

            DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(deleteRequest);

            return generateS3Url(key);
        } catch (RuntimeException e) {
            return "Ошибка : " + e.getMessage();
        }
    }

    private String extractKeyFromUrl(String fileRef) {
        if (fileRef.startsWith("http") || fileRef.startsWith("s3://")) {
            return fileRef.substring(fileRef.lastIndexOf("/") + 1);
        }
        return fileRef;
    }

    private String generateS3Url(String key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, key);
    }
}
