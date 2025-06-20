package com.withins.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(
        scanBasePackages = {
                "com.withins.api",
                "com.withins.core",
                "com.withins.crawl"
        }
)
@EnableScheduling
public class WithInsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WithInsApplication.class, args);
    }

}
