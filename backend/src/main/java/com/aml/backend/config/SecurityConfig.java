package com.aml.backend.config;

import com.aml.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter; // 🔑 Inject your filter

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF for APIs
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 🔑 stateless
                .authorizeHttpRequests(auth -> auth
                        // ✅ allow register & login without authentication
                        .requestMatchers("/api/auth/**").permitAll()

                        // ✅ allow Spring Boot error page
                        .requestMatchers("/error").permitAll()

                        // ✅ protect users endpoints
                        .requestMatchers(HttpMethod.GET, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()

                        // ✅ everything else needs authentication
                        .anyRequest().authenticated()
                );

        // 🔑 Register the JWT filter in the chain
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
