package com.withins.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withins.api.auth.JwtApiVersion;
import com.withins.api.auth.auth2.PrincipalDetailsService;
import com.withins.api.auth.auth2.PrincipalOAuth2UserService;
import com.withins.api.auth.jwt.CustomJwtAuthenticationConverter;
import com.withins.api.auth.jwt.JwtTokenProvider;
import com.withins.api.auth.jwt.formLogin.JsonAuthenticationFilter;
import com.withins.api.auth.jwt.logout.JsonLogoutSuccessHandler;
import com.withins.api.auth.jwt.logout.JwtTokenClearingLogoutHandler;
import com.withins.api.auth.jwt.oauth.CookieBearerTokenResolver;
import com.withins.api.auth.jwt.oauth.OAuth2AuthenticationSuccessHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Profile("!test")
public class SecurityConfig {

    private final JwtApiVersion version;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider provider;
    private final ObjectMapper mapper;

    private final PrincipalDetailsService principalDetailsService;
    private final PrincipalOAuth2UserService principalOAuth2UserService;

    private final JwtTokenClearingLogoutHandler jwtTokenClearingLogoutHandler;
    private final JsonLogoutSuccessHandler jsonLogoutSuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var authenticationManager = getAuthenticationManager(http);
        var filter = jsonAuthenticationFilter(authenticationManager);

        //TODO CSRF 보호 관련 설정 필요, CSRF 토큰보다는 SameSite 고려
        http
            .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)

            .csrf(AbstractHttpConfigurer::disable)

            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers( "/favicon.ico","/static/**", "/*.js", "/*.css").permitAll()
                .requestMatchers("/api/v1/auth/**").authenticated()
                .requestMatchers("/api/v1/posts/**").permitAll()
                .anyRequest().permitAll()
            )

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .oauth2Login(login -> login
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(principalOAuth2UserService)
                )
                // OAuth2 유저 -> JWT 토큰 발급
                .successHandler(new OAuth2AuthenticationSuccessHandler(provider))
                .failureHandler((request, response, exception) -> {
                    response.sendRedirect("/login?error");
                })
            )

            .oauth2ResourceServer(server -> server
                .bearerTokenResolver(new CookieBearerTokenResolver())
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                )
            )

            .logout(logout -> logout
                .logoutUrl(version.generateAPIPath("/auth/logout"))
                .addLogoutHandler(jwtTokenClearingLogoutHandler)
                .logoutSuccessHandler(jsonLogoutSuccessHandler)
            )

            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )

            .authenticationManager(authenticationManager)
        ;
        return http.build();
    }

    AuthenticationManager getAuthenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(principalDetailsService).passwordEncoder(encoder);
        return authManagerBuilder.build();
    }

    JsonAuthenticationFilter jsonAuthenticationFilter(AuthenticationManager authenticationManager) {
        var filter = new JsonAuthenticationFilter(mapper, provider);
        filter.setAuthenticationManager(authenticationManager);
        filter.setFilterProcessesUrl(version.generateAPIPath("/login"));
        return filter;
    }

}