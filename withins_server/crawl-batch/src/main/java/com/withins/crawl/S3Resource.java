package com.withins.crawl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.AbstractResource;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class S3Resource extends AbstractResource {
    private final String bucketName;
    private final String objectKey;
    private final S3Client s3Client;

    public static S3Resource from(String s3Path, S3Client s3Client) {
        // path 형식 - "s3://crawl-json-bucket/crawling-results/2025-06-13/오정노인복지관/2025-06-15-17-20-12.json"
        String path = s3Path.substring(5); // "s3://" 제거
        int slashIndex = path.indexOf('/');

        return new S3Resource(
                path.substring(0, slashIndex),
                path.substring(slashIndex + 1),
                s3Client
        );
    }

    @Override
    public String getDescription() {
        return "S3 Resource: " + bucketName + "/" + objectKey;
    }

    @Override
    public InputStream getInputStream() {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();

        return s3Client.getObject(request);
    }
}
