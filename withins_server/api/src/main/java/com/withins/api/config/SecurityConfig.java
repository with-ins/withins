package com.withins.api.config;

import com.withins.api.auth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("!test")
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        //TODO CSRF 보호 관련 설정 필요, CSRF 토큰보다는 SameSite 고려
        http
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/error").permitAll()
                .requestMatchers("/api/auth/login/**", "/api/auth/logout/**").permitAll()
                .requestMatchers("/api/auth/user").permitAll()
                    .requestMatchers("/api/v1/posts/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo
                    .oidcUserService(customOAuth2UserService)
                )
                .defaultSuccessUrl("/api/v1/auth/login/success")
                .failureUrl("/api/v1/auth/login/failure")
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/api/v1/auth/logout"))
                .logoutSuccessUrl("/api/v1/auth/logout/success")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }
}