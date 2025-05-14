package com.Wasfa.front_end.Configuration;

import com.Wasfa.front_end.Securite.JwtAuthenticationFilter;
import com.Wasfa.front_end.Securite.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationProvider authenticationProvider;

    // Constructor injection for dependencies
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtTokenProvider jwtTokenProvider, AuthenticationProvider authenticationProvider) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())  // Disable CSRF for API with JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Stateless session for API
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/**"

                        ).permitAll()  // ✅ Ces routes sont publiques

                        .requestMatchers("/api/GESTIONAIRE/**").hasAnyRole("GESTIONAIRE", "ADMIN")
                        .requestMatchers("/api/VETERINAIRE/**").hasAnyRole("VETERINAIRE", "ADMIN")

                        .requestMatchers("/api/admin").hasRole("ADMIN") // ⚠ Placer APRÈS les autres /api/xxx
                        .anyRequest().authenticated()
                )

                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)  // Add JWT filter before standard authentication
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
