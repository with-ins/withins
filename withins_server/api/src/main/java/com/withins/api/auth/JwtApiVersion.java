package com.withins.api.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtApiVersion {

    @Value("${security.api.version}")
    private String apiVersion;

    public String generateAPIPath(String path) {
        return String.format("/api/%s%s", apiVersion, path);
    }
}
