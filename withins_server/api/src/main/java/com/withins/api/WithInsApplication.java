package com.withins.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
        scanBasePackages = {
                "com.withins.api",
                "com.withins.core"
        }
)
public class WithInsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WithInsApplication.class, args);
    }

}
