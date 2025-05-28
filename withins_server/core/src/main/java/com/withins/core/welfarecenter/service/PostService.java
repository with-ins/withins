package com.withins.core.welfarecenter.service;

import com.withins.core.paging.PageParams;
import com.withins.core.paging.PageResponse;
import com.withins.core.welfarecenter.component.PostReader;
import com.withins.core.welfarecenter.dto.GetPostJobsResponse;
import com.withins.core.welfarecenter.dto.GetPostNoticesResponse;
import com.withins.core.welfarecenter.dto.GetPostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostReader postReader;

    public PageResponse<GetPostsResponse> getPostingsRespWithPage(final PageParams pageParams) {
        return postReader.readPostsRespWithPage(pageParams.page(), pageParams.size());
    }

    public PageResponse<GetPostNoticesResponse> getPostNoticesRespWithPage(final PageParams pageParams) {
        return postReader.readPostNoticesRespWithPage(pageParams.page(), pageParams.size());
    }

    public PageResponse<GetPostJobsResponse> getPostJobsRespWithPage(final PageParams pageParams) {
        return postReader.readPostJobsRespWithPage(pageParams.page(), pageParams.size());
    }
}