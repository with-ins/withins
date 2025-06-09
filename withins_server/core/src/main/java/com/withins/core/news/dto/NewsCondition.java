package com.withins.core.news.dto;

import com.withins.core.paging.Condition;
import com.withins.core.news.enums.KoreanRegion;
import com.withins.core.news.enums.NewsType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewsCondition implements Condition {

    private String word;
    private KoreanRegion region;
    private NewsType type;

}
