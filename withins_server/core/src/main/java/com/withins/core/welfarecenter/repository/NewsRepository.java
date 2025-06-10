package com.withins.core.welfarecenter.repository;

import com.withins.core.welfarecenter.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
