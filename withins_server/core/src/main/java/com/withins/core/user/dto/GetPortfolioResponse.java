package com.withins.core.user.dto;

import com.withins.core.user.entity.Portfolio;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetPortfolioResponse {
    private Long portfolioId;
    private String title;
    private String content;
    private Long userId;
    private String userNickname;

    public static GetPortfolioResponse from(final Portfolio portfolio) {
        return new GetPortfolioResponse(
                portfolio.getId(),
                portfolio.getTitle(),
                portfolio.getContent(),
                portfolio.getUser().getId(),
                portfolio.getUser().getNickname()
        );
    }
}
