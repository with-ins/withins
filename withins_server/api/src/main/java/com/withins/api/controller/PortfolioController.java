package com.withins.api.controller;

import com.withins.core.user.dto.GetPortfolioResponse;
import com.withins.core.user.service.PortfolioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @GetMapping("/{portfolioId}")
    public ResponseEntity<GetPortfolioResponse> getPortfolio(@PathVariable final Long portfolioId) {
        final GetPortfolioResponse response = portfolioService.getPortfolio(portfolioId);
        return ResponseEntity.ok(response);
    }
}
