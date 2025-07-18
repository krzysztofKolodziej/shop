package com.Shop.shop.config;

import com.Shop.shop.service.user.token.TokenBlacklistService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.mock;

@TestConfiguration
@EnableWebSecurity
@ActiveProfiles("test")
public class TestSecurityConfig {

    @Bean
    @Primary
    public TokenBlacklistService tokenBlacklistService() {
        return mock(TokenBlacklistService.class);
    }

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        return mock(JavaMailSender.class);
    }

    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/signup", "/login", "/verify-email", "/reset-password", "/reset-password-check").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}