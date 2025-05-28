package com.withins.core.welfarecenter.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.withins.core.paging.PageResponse;
import com.withins.core.paging.PageUtils;
import com.withins.core.welfarecenter.dto.GetPostJobsResponse;
import com.withins.core.welfarecenter.dto.GetPostNoticesResponse;
import com.withins.core.welfarecenter.dto.GetPostsResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import static com.withins.core.welfarecenter.entity.QPost.post;
import static com.withins.core.welfarecenter.entity.QPostJob.postJob;
import static com.withins.core.welfarecenter.entity.QPostNotice.postNotice;

@Repository
@RequiredArgsConstructor
public class PostQDSLRepository {

    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    public PageResponse<GetPostsResponse> findPostsRespWithPage(final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        final JPAQuery<GetPostsResponse> query = queryFactory
                .select(Projections.fields(GetPostsResponse.class,
                        post.id.as("postId"),
                        post.title.as("title"),
                        post.url.as("url"),
                        post.welfareCenter.id.as("welfareCenterId"),
                        post.welfareCenter.name.as("welfareCenterName"),
                        post.welfareCenter.region.as("region"),
                        post.category.as("category"),
                        post.createdDate.as("registrationDate")
                ))
                .from(post);

        final JPAQuery<Long> countQuery = queryFactory
                .select(post.count())
                .from(post);

        final Page<GetPostsResponse> resultPage = PageUtils.toPage(query, countQuery, pageable);
        return new PageResponse<>(
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getContent()
        );
    }

    public PageResponse<GetPostNoticesResponse> findPostNoticesRespWithPage(final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        final JPAQuery<GetPostNoticesResponse> query = queryFactory
                .select(Projections.fields(GetPostNoticesResponse.class,
                        postNotice.id.as("postId"),
                        postNotice.title.as("title"),
                        postNotice.url.as("url"),
                        postNotice.welfareCenter.id.as("welfareCenterId"),
                        postNotice.welfareCenter.name.as("welfareCenterName"),
                        postNotice.welfareCenter.region.as("region"),
                        postNotice.category.as("category"),
                        postNotice.createdDate.as("registrationDate")
                ))
                .from(postNotice);

        final JPAQuery<Long> countQuery = queryFactory
                .select(postNotice.count())
                .from(postNotice);

        final Page<GetPostNoticesResponse> resultPage = PageUtils.toPage(query, countQuery, pageable);
        return new PageResponse<>(
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getContent()
        );
    }

    public PageResponse<GetPostJobsResponse> findPostJobsRespWithPage(final int page, final int size) {
        final Pageable pageable = PageRequest.of(page, size);

        final JPAQuery<GetPostJobsResponse> query = queryFactory
                .select(Projections.fields(GetPostJobsResponse.class,
                        postJob.id.as("postId"),
                        postJob.title.as("title"),
                        postJob.url.as("url"),
                        postJob.welfareCenter.id.as("welfareCenterId"),
                        postJob.welfareCenter.name.as("welfareCenterName"),
                        postJob.welfareCenter.region.as("region"),
                        postJob.recruitmentStartDate.as("recruitmentStartDate"),
                        postJob.recruitmentEndDate.as("recruitmentEndDate"),
                        postJob.createdDate.as("registrationDate")
                ))
                .from(postJob);

        final JPAQuery<Long> countQuery = queryFactory
                .select(postJob.count())
                .from(postJob);

        final Page<GetPostJobsResponse> resultPage = PageUtils.toPage(query, countQuery, pageable);
        return new PageResponse<>(
                resultPage.getTotalPages(),
                resultPage.getTotalElements(),
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getContent()
        );
    }
}
