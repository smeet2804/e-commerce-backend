package com.example.ecommercebackend.config;

import com.example.ecommercebackend.repository.UserRepository;
import com.example.ecommercebackend.service.CustomUserDetailsService;
import com.example.ecommercebackend.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
Setting up google oauth 2.0:
https://medium.com/@anupama.pathirage/using-oauth-2-0-to-access-google-apis-1dbd01edea9a

Setting up postman client to get authorization token
https://learning.postman.com/docs/sending-requests/authorization/oauth-20/

https://stackoverflow.com/questions/32076503/using-postman-to-access-oauth-2-0-google-apis

https://medium.com/@sallu-salman/implementing-sign-in-with-google-in-spring-boot-application-5f05a34905a8
*/

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
                        .requestMatchers("/role-check/admin").hasRole("ADMIN")
                        .requestMatchers("/role-check/user").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                )
                .addFilterBefore(customJwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

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
