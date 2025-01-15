package com.librarymanagement.LibraryManagement.config;

import com.librarymanagement.LibraryManagement.user.DatabaseUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        //http.authorizeHttpRequests((requests) -> requests.anyRequest().authenticated());
        http.authorizeHttpRequests((requests) -> requests
                        //Get na /api/authors/** oraz /api/books/** - dostęp bez logowania (anonimowy)
                        .requestMatchers(HttpMethod.GET, "/api/authors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/api/**").permitAll()

                        //Post dla register i login
                        .requestMatchers(HttpMethod.POST, "/api/register").permitAll()
                        .requestMatchers("/api/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/logout").permitAll()

                        // POST, PUT, DELETE dla api/authors/** i dla /api/books/**
                        .requestMatchers(HttpMethod.POST, "/api/authors/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/authors/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/authors/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/books/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").authenticated()
                ).csrf().disable()
                .cors(cors -> cors.configurationSource(request -> {
                    var config = new org.springframework.web.cors.CorsConfiguration();
                    config.setAllowCredentials(true);
                    config.setAllowedOrigins(List.of("http://localhost:5173"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
                    return config;
                }))
                .formLogin(withDefaults());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(DatabaseUserDetailsService databaseUserDetailsService,
                                                            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(databaseUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    // Rejestracja managera i providera
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,
                                                       DaoAuthenticationProvider authProvider) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .authenticationProvider(authProvider)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}

