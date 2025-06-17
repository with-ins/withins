package com.withins.crawl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.Resource;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class S3NewsFileItemReader implements ItemReader<List<S3NewsFileDto>> {
    private final List<Resource> resources;
    private int currentResourceIndex = 0;
    private final ObjectMapper objectMapper;

    public S3NewsFileItemReader(List<Resource> resources, ObjectMapper objectMapper) {
        this.resources = resources != null ? resources : Collections.emptyList();
        this.objectMapper = objectMapper;
    }

    @Override
    public List<S3NewsFileDto> read() throws Exception {
        if (currentResourceIndex >= resources.size()) {
            return null; // 모든 리소스를 처리했으면 null 반환
        }

        Resource resource = resources.get(currentResourceIndex);
        try (InputStream inputStream = resource.getInputStream()) {
            List<S3NewsFileDto> s3NewsFileDtos = objectMapper.readValue(inputStream, new TypeReference<List<S3NewsFileDto>>() {
            });
            // 리소스 처리 성공 후에 인덱스를 증가시켜야함. S3에 접근하지 못했을 때, 재시도 로직과 관련있음
            currentResourceIndex++;
            return s3NewsFileDtos;
        }
    }
}
