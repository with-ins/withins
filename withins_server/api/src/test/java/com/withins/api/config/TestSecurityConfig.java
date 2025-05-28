package com.withins.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@TestConfiguration
public class TestSecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new TestSecurityFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/posts/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    public static class TestSecurityFilter extends OncePerRequestFilter {
        public static final String TEST_AUTH_HEADER = "X-TEST-USER";
        public static final String TEST_AUTH_VALUE = "TEST-USER";
        public static final String TEST_AUTH_ROLE = "ROLE_USER";

        @Override
        protected void doFilterInternal(HttpServletRequest req,
                                        HttpServletResponse res,
                                        FilterChain chain) throws java.io.IOException, ServletException {
            String user = req.getHeader(TEST_AUTH_HEADER);
            if (user != null) {
                var auth = new UsernamePasswordAuthenticationToken(
                        user, null,
                        AuthorityUtils.createAuthorityList(TEST_AUTH_ROLE)
                );
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            chain.doFilter(req, res);
        }
    }
}
