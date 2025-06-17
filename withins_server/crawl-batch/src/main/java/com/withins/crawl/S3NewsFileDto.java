package com.withins.crawl;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class S3NewsFileDto {
    private String title;
    @JsonProperty("category")
    private String type;
    @JsonProperty("createdAt")
    private LocalDateTime newsCreatedAt;
    private String link;
    @JsonProperty("institutionName")
    private String welfareCenterName;
}
