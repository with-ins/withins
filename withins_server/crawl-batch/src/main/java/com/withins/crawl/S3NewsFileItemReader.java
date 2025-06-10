package com.withins.crawl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.batch.item.ItemReader;
import org.springframework.core.io.Resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class S3NewsFileItemReader implements ItemReader<List<NewsDto>> {
    private final List<Resource> resources;
    private int currentResourceIndex = 0;

    public S3NewsFileItemReader(List<Resource> resources) {
        this.resources = resources != null ? resources : Collections.emptyList();
    }

    @Override
    public List<NewsDto> read() throws Exception {
        if (currentResourceIndex >= resources.size()) {
            return null; // 모든 리소스를 처리했으면 null 반환
        }

        Resource resource = resources.get(currentResourceIndex++);
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            
            // 파일 내용을 문자열로 읽기
            String jsonContent = reader.lines().collect(Collectors.joining());
            
            // JSON 배열 파싱
            JSONArray jsonArray = new JSONArray(jsonContent);
            List<NewsDto> newsDtos = new ArrayList<>();
            
            // 각 JSON 객체를 NewsDto로 변환
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                NewsDto newsDto = new NewsDto();
                newsDto.setWelfareCenterName(jsonObject.optString("institutionName"));
                newsDto.setTitle(jsonObject.optString("title"));
                newsDto.setLink(jsonObject.optString("link"));
                newsDto.setType(jsonObject.optString("category"));

                newsDtos.add(newsDto);
            }
            
            return newsDtos;
        }
    }
}
