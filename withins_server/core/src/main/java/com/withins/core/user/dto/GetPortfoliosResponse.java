package com.withins.core.user.dto;

import com.withins.core.user.entity.Portfolio;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetPortfoliosResponse {
    private List<PortfolioInfo> portfolioInfos = new ArrayList<>();

    public static GetPortfoliosResponse from(List<Portfolio> portfolios) {
        GetPortfoliosResponse response = new GetPortfoliosResponse();
        response.portfolioInfos = portfolios.stream()
                .map(GetPortfoliosResponse::toPortfolioInfo)
                .collect(Collectors.toList());

        return response;
    }

    private static PortfolioInfo toPortfolioInfo(Portfolio portfolio) {
        return PortfolioInfo.builder()
                .id(portfolio.getId())
                .title(portfolio.getTitle())
                .build();
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    private static class PortfolioInfo {
        private Long id;
        private String title;
    }
}
