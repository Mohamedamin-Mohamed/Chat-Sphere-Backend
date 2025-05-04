package com.ChatSphere.Backend.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilesUploadService {
    private final S3Client s3Client;

    @Value("${AWS.s3BucketName}")
    String bucketName;

    @Value("${AWS.s3BucketKeyName}")
    String keyName;

    @Value("${AWS.region}")
    String region;

    public String uploadFileToS3Bucket(MultipartFile multipartFile) {
        log.info("Uploading file to s3 bucket");
        if (multipartFile.isEmpty()) return "";
        try {
            String fileName = multipartFile.getOriginalFilename();
            String s3Key = keyName + fileName;

            PutObjectRequest putObjectRequest = PutObjectRequest.builder().
                    bucket(bucketName).
                    key(s3Key).
                    contentType(multipartFile.getContentType()).
                    build();

            PutObjectResponse putObjectResponse = s3Client.
                    putObject(putObjectRequest,
                            /* avoids memory issues as it streams the file directly without
                            loading the entire content into memory. */
                            RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
            if (putObjectResponse.sdkHttpResponse().isSuccessful()) {
                return getPublicUrl(s3Key);
            }
            return "";
        } catch (Exception exp) {
            log.error("Error uploading file to s3: ", exp);
            return "";
        }
    }

    private String getPublicUrl(String s3Key) {
        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, s3Key);
    }
}
