package com.example.ecommercebackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/*
Setting up google oauth 2.0:
https://medium.com/@anupama.pathirage/using-oauth-2-0-to-access-google-apis-1dbd01edea9a

Setting up postman client to get authorization token
https://learning.postman.com/docs/sending-requests/authorization/oauth-20/

https://stackoverflow.com/questions/32076503/using-postman-to-access-oauth-2-0-google-apis
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/role-check/admin").hasRole("ADMIN")
                        .requestMatchers("/role-check/user").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2ResourceServerConfigurer -> oauth2ResourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder())
                        )
                );

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return JwtDecoders.fromIssuerLocation("https://accounts.google.com");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
