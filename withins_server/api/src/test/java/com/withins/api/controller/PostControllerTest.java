package com.withins.api.controller;

import com.withins.api.config.RestAssuredTest;
import com.withins.core.paging.PageResponse;
import com.withins.core.welfarecenter.dto.GetPostJobsResponse;
import com.withins.core.welfarecenter.dto.GetPostNoticesResponse;
import com.withins.core.welfarecenter.dto.GetPostsResponse;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

class PostControllerTest extends RestAssuredTest {

    @Test
    @Sql(scripts = {"classpath:sql/post-controller.sql"})
    void 전체_게시물_목록조회_페이징_API() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                .log().all()
                .get("/api/v1/posts?page=0&size=3")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        PageResponse<GetPostsResponse> pageResponse = response.as(new TypeRef<>() {});
        assertThat(pageResponse.content()).hasSize(3);

        GetPostsResponse getPostsResponse = pageResponse.content().get(0);
        assertThat(getPostsResponse.getPostId()).isEqualTo(1);
        assertThat(getPostsResponse.getTitle()).isEqualTo("첫 번째 공지사항");
        assertThat(getPostsResponse.getWelfareCenterName()).isEqualTo("복지센터1");
        assertThat(getPostsResponse.getRegion()).isEqualTo("서울");
        assertThat(getPostsResponse.getUrl()).isEqualTo("https://example.com/notice1");
        assertThat(getPostsResponse.getCategory()).isEqualTo("notice");
        assertThat(getPostsResponse.getRegistrationDate()).isNotNull();
    }

    @Test
    @Sql(scripts = {"classpath:sql/post-controller.sql"})
    void 공지사항_목록조회_페이징_API() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                .log().all()
                .get("/api/v1/posts/notice?page=0&size=2")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        PageResponse<GetPostNoticesResponse> pageResponse = response.as(new TypeRef<>() {});
        assertThat(pageResponse.content()).hasSize(2);

        GetPostNoticesResponse getPostsResponse = pageResponse.content().get(1);
        assertThat(getPostsResponse.getPostId()).isEqualTo(2);
        assertThat(getPostsResponse.getTitle()).isEqualTo("두 번째 공지사항");
        assertThat(getPostsResponse.getWelfareCenterName()).isEqualTo("복지센터1");
        assertThat(getPostsResponse.getRegion()).isEqualTo("서울");
        assertThat(getPostsResponse.getUrl()).isEqualTo("https://example.com/notice2");
        assertThat(getPostsResponse.getCategory()).isEqualTo("notice");
        assertThat(getPostsResponse.getRegistrationDate()).isNotNull();
    }

    @Test
    @Sql(scripts = {"classpath:sql/post-controller.sql"})
    void 채용_목록조회_페이징_API() {
        // when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .when()
                .log().all()
                .get("/api/v1/posts/job?page=0&size=2")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        // then
        PageResponse<GetPostJobsResponse> pageResponse = response.as(new TypeRef<>() {});
        assertThat(pageResponse.content()).hasSize(2);

        GetPostJobsResponse getPostJobsResponse = pageResponse.content().get(0);
        assertThat(getPostJobsResponse.getPostId()).isEqualTo(3);
        assertThat(getPostJobsResponse.getTitle()).isEqualTo("첫 번째 채용공고");
        assertThat(getPostJobsResponse.getWelfareCenterName()).isEqualTo("복지센터1");
        assertThat(getPostJobsResponse.getRegion()).isEqualTo("서울");
        assertThat(getPostJobsResponse.getRecruitmentStartDate()).isNotNull();
        assertThat(getPostJobsResponse.getRecruitmentEndDate()).isNotNull();
        assertThat(getPostJobsResponse.getRegistrationDate()).isNotNull();
    }
}
