package com.withins.core.builders;

import com.withins.core.news.entity.News;
import com.withins.core.news.enums.KoreanRegion;
import com.withins.core.news.enums.NewsType;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import com.withins.core.welfarecenter.repository.WelfareCenterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.time.LocalDate;

@TestComponent
public class NewsBuilder {

    @Autowired
    private WelfareCenterRepository welfareCenterRepository;
    
    public News with(String title, NewsType type, KoreanRegion region) {
        return getDefaultBuilder().title(title).type(type)
            .welfareCenter(welfareCenterRepository.save(getDefaultWelfareCenterBuilder().region(region).build()))
            .build();
    }

    private News.NewsBuilder getDefaultBuilder() {
        return News.builder().link("").newsCreatedAt(LocalDate.now());
    }

    private WelfareCenter.WelfareCenterBuilder getDefaultWelfareCenterBuilder() {
        return WelfareCenter.builder().address("어딘가 어딘가").url("google.com").name("무슨무슨복지관").email("asdf@gmail.com");
    }
}
