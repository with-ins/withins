package com.withins.core.paging;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.withins.core.config.IntegrationTest;
import com.withins.core.news.entity.News;
import com.withins.core.news.enums.KoreanRegion;
import com.withins.core.welfarecenter.entity.WelfareCenter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static com.withins.core.builders.NewsBuilder.News;
import static com.withins.core.builders.WelfareCenterBuilder.WelfareCenter;
import static com.withins.core.news.entity.QNews.news;
import static com.withins.core.news.enums.KoreanRegion.BUCHEON;
import static com.withins.core.news.enums.KoreanRegion.SEOUL;
import static com.withins.core.welfarecenter.entity.QWelfareCenter.welfareCenter;
import static org.assertj.core.api.Assertions.assertThat;

class PageUtilsTest extends IntegrationTest {

    @Autowired
    private PageUtils sut;

    private JPAQuery<News> generateNewsQuery(BooleanExpression... where) {
        return testSupport.jpaQueryFactory
                .selectFrom(news)
                .join(news.welfareCenter, welfareCenter)
                .where(where == null ? new BooleanExpression[]{} : where);
    }

    private JPAQuery<WelfareCenter> generateWelfareQuery(BooleanExpression... where) {
        return testSupport.jpaQueryFactory
                .selectFrom(welfareCenter)
                .where(where == null ? new BooleanExpression[]{} : where);
    }


    @Test
    void 조건이_없다면_모든_데이터를_반환한다() {
        // given
        testSupport.saveAll(
                WelfareCenter().build(),
                WelfareCenter().build()
        );

        // when
        Page<WelfareCenter> newsPage = sut.of(
                PageRequest.of(0, 10),
                generateWelfareQuery((BooleanExpression) null),
                welfareCenter);

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(2);
        assertThat(newsPage.getNumberOfElements()).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3})
    void pageSize보다_저장된_엔티티들의_크기가_클_경우_content는_pageSize만큼만_반환한다(int pageSize) {
        // given
        testSupport.saveAll(
                WelfareCenter().build(),
                WelfareCenter().build(),
                WelfareCenter().build()
        );

        // when
        Page<WelfareCenter> newsPage = sut.of(
                PageRequest.of(0, pageSize),
                generateWelfareQuery((BooleanExpression) null),
                welfareCenter
        );

        // then
        assertThat(newsPage.getNumberOfElements()).isEqualTo(pageSize);
    }

    @Test
    void pageSize가_저장된_엔티티들의_크기보다_클_경우_content는_저장된_엔티티의_크기와_같다() {
        // given
        testSupport.save(
                WelfareCenter().build()
        );

        // when
        Page<WelfareCenter> newsPage = sut.of(
                PageRequest.of(0, 2),
                generateWelfareQuery((BooleanExpression) null),
                welfareCenter
        );

        // then
        assertThat(newsPage.getNumberOfElements()).isEqualTo(1);
    }

    @Test
    void Text_필터테스트_띄어쓰기를_무시해야한다() {
        // given
        WelfareCenter center = testSupport.save(
                WelfareCenter().build()
        );

        testSupport.saveAll(
                News().withWelfareCenter(center).withTitle("ABC DEF").withLink("link1").build(),
                News().withWelfareCenter(center).withTitle("ABCDEF").withLink("link2").build()
        );

        // when
        BooleanExpression wordFilter = sut.filter(news.title, "ABCDE F", true);
        Page<News> newsPage = sut.of(
                PageRequest.of(0, 10),
                generateNewsQuery(wordFilter),
                news);

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(2);
    }

    @Test
    void Test_필터테스트_띄어쓰기까지_포함하여_필터링한다() {
        // given
        testSupport.saveAll(
                WelfareCenter().withName("ABC DEF").build(),
                WelfareCenter().withName("ABCDEF").build()
        );

        // when
        BooleanExpression wordFilter = sut.filter(welfareCenter.name, "ABCDEF", false);
        Page<WelfareCenter> newsPage = sut.of(
                PageRequest.of(0, 10),
                generateWelfareQuery(wordFilter),
                welfareCenter);

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(1);
    }

    @Test
    void Enum_필터테스트_필터링_조건에_포함된_모든_데이터를_가져온다() {
        // given
        testSupport.saveAll(
                WelfareCenter().withRegion(SEOUL).build(),
                WelfareCenter().withRegion(BUCHEON).build()
        );

        // when
        KoreanRegion regionFilter = BUCHEON;

        Page<WelfareCenter> newsPage = sut.of(
                PageRequest.of(0, 10),
                generateWelfareQuery(sut.filter(welfareCenter.region, regionFilter)),
                welfareCenter
        );

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(1);
    }

    /**
     * KoreanRegion.ALL, NewsType.ALL 는 존재할 수 없는 값이다. 이는 논리적 정합성을 유지하기 위해 사용되므로
     * Enum 에 값을 유지해야한다. 따라서 유저 요청에 이에 해당되는 값이 들어온다면 이는 조건식을 null 처리해야할 필요가 있다.
     * 이때 사용되는것이 filter이다.
     */
    @Test
    void Enum_필터테스트_비교해야할_데이터가_제외항목에_존재하면_null을_반환한다() {
        // given
        testSupport.saveAll(
                WelfareCenter().withRegion(SEOUL).build(),
                WelfareCenter().withRegion(BUCHEON).build()
        );

        // when
        KoreanRegion regionFilter = KoreanRegion.ALL;
        KoreanRegion exclude = KoreanRegion.ALL;

        Page<WelfareCenter> newsPage = sut.of(
                PageRequest.of(0, 10),
                generateWelfareQuery(sut.filter(news.welfareCenter.region, regionFilter, exclude)),
                welfareCenter
        );

        // then
        assertThat(newsPage.getTotalElements()).isEqualTo(2);
    }
}