package com.Wasfa.front_end.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Autoriser les origines

        config.addAllowedOrigin("http://localhost:3000"); // Remplacer par l'origine de ton frontend
        config.addAllowedMethod("*"); // Autoriser toutes les méthodes HTTP (GET, POST, etc.)
        config.addAllowedHeader("*"); // Autoriser tous les headers
        config.setAllowCredentials(true); // Permet l'inclusion des cookies

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
