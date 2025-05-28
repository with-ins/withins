package com.withins.core.user.dto;

import com.withins.core.user.entity.Portfolio;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PortfolioResponse(
        Long id,
        String title,
        String content,
        Long userId,
        String userNickname,
        LocalDateTime createdDate,
        LocalDateTime lastModifiedDate
) {
    public static PortfolioResponse from(final Portfolio portfolio) {
        return PortfolioResponse.builder()
                .id(portfolio.getId())
                .title(portfolio.getTitle())
                .content(portfolio.getContent())
                .userId(portfolio.getUser().getId())
                .userNickname(portfolio.getUser().getNickname())
                .createdDate(portfolio.getCreatedDate())
                .lastModifiedDate(portfolio.getLastModifiedDate())
                .build();
    }
}
