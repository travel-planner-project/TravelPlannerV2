package com.travelplanner.v2.global.util;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;

@Service
public class S3Util {
    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public void uploadFile(String keyName, String filePath, String directory) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        S3Client s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(directory + keyName) // 변경된 keyName 경로
                .build();

        s3.putObject(putObjectRequest, Paths.get(filePath));
    }

    public void deleteFile(String keyName, String directory) {
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);

        S3Client s3 = S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(awsCreds))
                .build();

        DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                .bucket(bucketName)
                .key(directory + keyName)
                .build();

        s3.deleteObject(deleteObjectRequest);
    }

    public String generateUniqueImgName(String originalImgName, Long loginUserId) {
        return loginUserId + "_" + LocalDate.now() + "_" + System.currentTimeMillis() + getFileExtension(originalImgName);
    }

    public String getFileExtension(String imgName) {

        int dotIndex = imgName.lastIndexOf('.');
        return dotIndex == -1 ? "" : imgName.substring(dotIndex);
    }

    public void deleteLocalFile(String localFilePath) {

        File file = new File(localFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

    // 프로필 이미지 url 에서 키네임을 추출하는 메서드 입니다.
    public String extractKeyNameFromProfileImageUrl(String profileImageUrl) {
        String baseUrl = "https://travel-planner-buckets.s3.ap-northeast-2.amazonaws.com/";
        if (profileImageUrl != null && profileImageUrl.startsWith(baseUrl)) {
            return profileImageUrl.substring(baseUrl.length());
        }
        return "";
    }
}