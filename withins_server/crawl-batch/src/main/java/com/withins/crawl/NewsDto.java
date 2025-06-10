package com.withins.crawl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NewsDto {
    private Long baseId;
    private String title;
    private String type;
    private String link;
    private String welfareCenterName;
}