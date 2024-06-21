package com.example.ecommercebackend.config;

import com.example.ecommercebackend.repository.UserRepository;
import com.example.ecommercebackend.config.CustomUserDetailsService;
import com.example.ecommercebackend.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, UserService userService, UserRepository userRepository) throws Exception {
        System.out.println("Configuring SecurityFilterChain");

        CustomJwtAuthenticationFilter customJwtAuthenticationFilter = new CustomJwtAuthenticationFilter(userRepository);

        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .anyRequest().authenticated()
                );

        System.out.println("SecurityFilterChain configured");

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        System.out.println("Creating JwtDecoder");
        return JwtDecoders.fromIssuerLocation("https://accounts.google.com");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        System.out.println("Creating BCryptPasswordEncoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        System.out.println("Creating JwtAuthenticationConverter");
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new CustomJwtAuthenticationConverter());
        return jwtAuthenticationConverter;
    }
}
