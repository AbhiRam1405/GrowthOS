package com.growthtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * CORS configuration to allow the Vite frontend at localhost:5173
 * to communicate with the Spring Boot backend at localhost:8080.
 */
@Configuration
public class CorsConfig {

    @org.springframework.beans.factory.annotation.Value("${cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        
        if (allowedOrigins != null && !allowedOrigins.isEmpty()) {
            String[] origins = allowedOrigins.split(",");
            for (String origin : origins) {
                config.addAllowedOrigin(origin.trim());
            }
        }
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
