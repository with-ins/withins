package com.withins.core.user.component;

import com.withins.core.user.entity.Portfolio;
import com.withins.core.user.entity.User;
import com.withins.core.user.repository.PortfolioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class PortfolioWriter {
    private final UserReader userReader;
    private final PortfolioRepository portfolioRepository;

    @Transactional
    public Long save(final Long userId, final String title, final String content) {
        final User user = userReader.read(userId);
        final Portfolio portfolio = Portfolio.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();

        final Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        log.info("포트폴리오 생성 완료 - portfolioId={}, userId={}",
                savedPortfolio.getId(), savedPortfolio.getUser().getId());

        return savedPortfolio.getId();
    }
}
