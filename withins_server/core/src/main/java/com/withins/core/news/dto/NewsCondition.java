package com.withins.core.news.dto;

import com.withins.core.paging.Condition;
import com.withins.core.news.enums.KoreanRegion;
import com.withins.core.news.enums.NewsType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewsCondition implements Condition {

    private String word;
    private KoreanRegion region;
    private NewsType type;

}
