package com.withins.api.controller;

import com.withins.core.paging.PageParams;
import com.withins.core.paging.PageResponse;
import com.withins.core.welfarecenter.dto.GetPostJobsResponse;
import com.withins.core.welfarecenter.dto.GetPostNoticesResponse;
import com.withins.core.welfarecenter.dto.GetPostsResponse;
import com.withins.core.welfarecenter.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping
    public ResponseEntity<PageResponse<GetPostsResponse>> getPostingsRespWithPage(@ModelAttribute final PageParams pageParams) {
        final PageResponse<GetPostsResponse> response = postService.getPostingsRespWithPage(pageParams);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/notice")
    public ResponseEntity<PageResponse<GetPostNoticesResponse>> getPostNoticesRespWithPage(@ModelAttribute final PageParams pageParams) {
        final PageResponse<GetPostNoticesResponse> response = postService.getPostNoticesRespWithPage(pageParams);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/job")
    public ResponseEntity<PageResponse<GetPostJobsResponse>> getPostJobsRespWithPage(@ModelAttribute final PageParams pageParams) {
        final PageResponse<GetPostJobsResponse> response = postService.getPostJobsRespWithPage(pageParams);
        return ResponseEntity.ok(response);
    }
}
