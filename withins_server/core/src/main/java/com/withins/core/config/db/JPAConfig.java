package com.withins.core.config.db;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan(basePackages = "com.withins.core")
@EnableJpaRepositories(basePackages = "com.withins.core")
@EnableJpaAuditing
public class JPAConfig {
}
