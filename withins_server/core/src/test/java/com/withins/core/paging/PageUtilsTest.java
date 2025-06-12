package com.withins.core.paging;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.withins.core.builders.NewsBuilder;
import com.withins.core.config.IntegrationTest;
import com.withins.core.news.entity.News;
import com.withins.core.news.enums.KoreanRegion;
import com.withins.core.news.enums.NewsType;
import com.withins.core.news.repository.NewsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.withins.core.news.entity.QNews.news;
import static com.withins.core.news.enums.KoreanRegion.*;
import static com.withins.core.news.enums.NewsType.*;
import static com.withins.core.welfarecenter.entity.QWelfareCenter.welfareCenter;
import static org.assertj.core.api.Assertions.*;

@Transactional
class PageUtilsTest extends IntegrationTest {

    @Autowired
    private PageUtils pageUtils;
    @Autowired
    private NewsRepository newsRepository;
    @Autowired
    private NewsBuilder newsBuilder;
    @Autowired
    private JPAQueryFactory query;

    private int expectedTotalPages(List<?> list, int pageSize) {
        int size = list.size();
        if (size % pageSize == 0) {
            return size / pageSize;
        }
        return size / pageSize + 1;
    }

    private JPAQuery<News> generateQuery(BooleanExpression... where) {
        return query.selectFrom(news)
            .join(news.welfareCenter, welfareCenter)
            .where(where == null ? new BooleanExpression[]{} : where);
    }

    @Test
    @DisplayName("조건이 없다면 모든 데이터를 반환한다.")
    void defaultPageWithTest() {
        // given
        int pageNumber = 0;
        int pageSize = 10;

        List<News> newsList = List.of(
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABC DEF", NOTICE, SEOUL)
        );
        newsRepository.saveAll(newsList);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        JPAQuery<News> jpaQuery = generateQuery((BooleanExpression) null);

        // when
        Page<News> newsPage = pageUtils.of(pageable, jpaQuery, news);

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(newsList.size());
        assertThat(newsPage.getNumberOfElements()).isEqualTo(newsList.size());
        assertThat(newsPage.getTotalPages()).isEqualTo(expectedTotalPages(newsList, pageSize));
    }

    @Test
    @DisplayName("pageSize 보다 리스트의 크기가 클 경우 content는 pageSize 만큼만 반환한다")
    void nextPageTest() {
        // given
        int pageNumber = 0;
        int pageSize = 2;

        List<News> newsList = List.of(
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABC DEF", NOTICE, SEOUL)
        );
        newsRepository.saveAll(newsList);

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        JPAQuery<News> jpaQuery = generateQuery((BooleanExpression) null);

        // when
        Page<News> newsPage = pageUtils.of(pageable, jpaQuery, news);

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(newsList.size());
        assertThat(newsPage.getNumberOfElements()).isEqualTo(pageSize);
        assertThat(newsPage.getTotalPages()).isEqualTo(expectedTotalPages(newsList, pageSize));
    }

    @Test
    @DisplayName("Text 필터 테스트 - 띄어쓰기를 무시해야한다.")
    void textFilterTest1() {
        // given
        List<News> newsList = List.of(
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL)
        );
        newsRepository.saveAll(newsList);

        String findWord = "ABCDE F";
        // when
        Pageable pageable = PageRequest.of(0, 10);

        BooleanExpression wordFilter = pageUtils.filter(news.title, findWord, true);
        Page<News> newsPage = pageUtils.of(pageable, generateQuery(wordFilter), news);

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(4);
    }

    @Test
    @DisplayName("Text 필터 테스트 - 띄어쓰기를 무시하면 안된다")
    void textFilterTest2() {
        // given
        List<News> newsList = List.of(
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL)
        );
        newsRepository.saveAll(newsList);

        String findWord = "ABCDEF";
        // when
        Pageable pageable = PageRequest.of(0, 10);

        BooleanExpression wordFilter = pageUtils.filter(news.title, findWord, false);
        Page<News> newsPage = pageUtils.of(pageable, generateQuery(wordFilter), news);

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(3);
    }

    @Test
    @DisplayName("Enum 필터 테스트 - 포함된 모든 데이터를 가져와야한다.")
    void enumFilterTest1() {
        // given
        List<News> newsList = List.of(
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, BUCHEON)
        );
        newsRepository.saveAll(newsList);

        KoreanRegion regionFilter = BUCHEON;

        // when
        Pageable pageable = PageRequest.of(0, 10);

        BooleanExpression wordFilter = pageUtils.filter(news.welfareCenter.region, regionFilter);
        Page<News> newsPage = pageUtils.of(pageable, generateQuery(wordFilter), news);

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    @DisplayName("Enum 필터 테스트 - 포함된 모든 데이터를 가져와야한다.2")
    void enumFilterTest2() {
        // given
        List<News> newsList = List.of(
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", EVENT, SEOUL),
            newsBuilder.with("ABCDEF", EVENT, BUCHEON)
        );
        newsRepository.saveAll(newsList);

        NewsType typeFilter = EVENT;

        // when
        Pageable pageable = PageRequest.of(0, 10);

        BooleanExpression wordFilter = pageUtils.filter(news.type, typeFilter);
        Page<News> newsPage = pageUtils.of(pageable, generateQuery(wordFilter), news);

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(2);
    }

    /**
     * KoreanRegion.ALL, NewsType.ALL 는 존재할 수 없는 값이다. 이는 논리적 정합성을 유지하기 위해 사용되므로
     * Enum 에 값을 유지해야한다. 따라서 유저 요청에 이에 해당되는 값이 들어온다면 이는 조건식을 null 처리해야할 필요가 있다.
     * 이때 사용되는것이 filter이다.
     */
    @Test
    @DisplayName("Enum 필터 테스트 - 비교해야할 데이터가 제외항목에 존재하면 null을 반환해야한다.")
    void enumFilterTest3() {
        // given
        List<News> newsList = List.of(
            newsBuilder.with("ABC DEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, SEOUL),
            newsBuilder.with("ABCDEF", NOTICE, BUCHEON)
        );
        newsRepository.saveAll(newsList);

        KoreanRegion regionFilter = KoreanRegion.ALL;
        KoreanRegion exclude = KoreanRegion.ALL;

        // when
        Pageable pageable = PageRequest.of(0, 10);

        BooleanExpression wordFilter = pageUtils.filter(news.welfareCenter.region, regionFilter, exclude);
        Page<News> newsPage = pageUtils.of(pageable, generateQuery(wordFilter), news);

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(newsList.size());
    }
}