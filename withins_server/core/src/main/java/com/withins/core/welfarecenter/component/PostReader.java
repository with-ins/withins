package com.withins.core.welfarecenter.component;

import com.withins.core.paging.PageResponse;
import com.withins.core.welfarecenter.dto.GetPostJobsResponse;
import com.withins.core.welfarecenter.dto.GetPostNoticesResponse;
import com.withins.core.welfarecenter.dto.GetPostsResponse;
import com.withins.core.welfarecenter.repository.PostQDSLRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostReader {

    private final PostQDSLRepository postQDSLRepository;
    
    public PageResponse<GetPostsResponse> readPostsRespWithPage(final int page, final int size) {
        log.debug("전체 게시물 목록 조회 실행 - page={}, size={}", page, size);

        final PageResponse<GetPostsResponse> response = postQDSLRepository.findPostsRespWithPage(page, size);

        log.debug("전체 게시물 목록 조회 완료 - totalPages: {}, totalElements: {}, page: {}, size: {}",
                response.totalPages(),
                response.totalElements(),
                response.number(),
                response.size());

        return response;
    }
    
    public PageResponse<GetPostNoticesResponse> readPostNoticesRespWithPage(final int page, final int size) {
        log.debug("공지사항 목록 조회 실행 - page={}, size={}", page, size);

        final PageResponse<GetPostNoticesResponse> response = postQDSLRepository.findPostNoticesRespWithPage(page, size);

        log.debug("공지사항 목록 조회 완료 - totalPages: {}, totalElements: {}, page: {}, size: {}",
                response.totalPages(),
                response.totalElements(),
                response.number(),
                response.size());

        return response;
    }

    public PageResponse<GetPostJobsResponse> readPostJobsRespWithPage(final int page, final int size) {
        log.debug("채용 목록 조회 실행 - page={}, size={}", page, size);

        final PageResponse<GetPostJobsResponse> response = postQDSLRepository.findPostJobsRespWithPage(page, size);

        log.debug("채용 목록 조회 완료 - totalPages: {}, totalElements: {}, page: {}, size: {}",
                response.totalPages(),
                response.totalElements(),
                response.number(),
                response.size());

        return response;
    }
}