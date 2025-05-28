package com.withins.core.user.component;

import com.withins.core.exception.EntityNotFoundException;
import com.withins.core.user.entity.Portfolio;
import com.withins.core.user.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PortfolioReader {

    private final PortfolioRepository portfolioRepository;

    @Transactional(readOnly = true)
    public Portfolio read(final Long portfolioId) {
        log.debug("포트폴리오 조회 실행 - id={}", portfolioId);

        final Portfolio portfolio = portfolioRepository.findWithUserBy(portfolioId)
                .orElseThrow(() -> new EntityNotFoundException("Portfolio", portfolioId));

        log.debug("포트폴리오 조회 완료 - id={}, userId={}",
                portfolio.getId(), portfolio.getUser().getId());

        return portfolio;
    }

    @Transactional(readOnly = true)
    public List<Portfolio> readBy(final Long userId) {
        log.debug("유저별 포트폴리오 목록 조회 실행 - userId={}", userId);

        final List<Portfolio> portfolios = portfolioRepository.findByUser_Id(userId);

        log.debug("유저별 포트폴리오 목록 조회 완료 - userId={}, count={}", userId, portfolios.size());

        return portfolios;
    }

    public Portfolio save(final Portfolio portfolio) {
        log.debug("포트폴리오 저장 실행 - title={}, userId={}",
                portfolio.getTitle(), portfolio.getUser().getId());

        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        log.debug("포트폴리오 저장 완료 - id={}, title={}", savedPortfolio.getId(), savedPortfolio.getTitle());

        return savedPortfolio;
    }
}