package com.withins.crawl;

import org.springframework.core.io.AbstractResource;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.IOException;
import java.io.InputStream;

public class S3Resource extends AbstractResource {
    private final String bucketName;
    private final String objectKey;
    private final S3Client s3Client;
    
    public S3Resource(String s3Path) {
        // s3Path 형식: s3://bucket-name/object-key
        // s3://crawl-json-bucket/crawling-results/2025-06-06/2025-06-09T13-14-18-653Z.json\
        String path = s3Path.substring(5); // "s3://" 제거
        int slashIndex = path.indexOf('/');
        
        this.bucketName = path.substring(0, slashIndex);
        this.objectKey = path.substring(slashIndex + 1);
        this.s3Client = S3Client.builder().build();
    }
    
    @Override
    public String getDescription() {
        return "S3 Resource: " + bucketName + "/" + objectKey;
    }
    
    @Override
    public InputStream getInputStream() throws IOException {
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(objectKey)
                .build();
        
        try {
            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(request);
            return s3Object;
        } catch (Exception e) {
            throw new IOException("Failed to get S3 object", e);
        }
    }
}