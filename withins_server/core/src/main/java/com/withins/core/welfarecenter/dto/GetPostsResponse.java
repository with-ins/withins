package com.withins.core.welfarecenter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPostsResponse {
    private Long postId;
    private String title;
    private String url;
    private Long welfareCenterId;
    private String welfareCenterName;
    private String region;
    private String category;
    private LocalDateTime registrationDate;
}