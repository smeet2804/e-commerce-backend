package com.example.ecommercebackend.config;

import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public CustomJwtAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("CustomJwtAuthenticationFilter: Request received");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof JwtAuthenticationToken) {
            System.out.println("Authentication is a JwtAuthenticationToken");
            Jwt jwt = (Jwt) authentication.getPrincipal();
            String email = jwt.getClaimAsString("email");
            System.out.println("JWT email claim: " + email);

            if (email != null) {
                User existingUser = userRepository.findByEmail(email);
                if (existingUser == null) {
                    System.out.println("User not found in database. Creating new user.");

                    String firstName = jwt.getClaimAsString("given_name");
                    String lastName = jwt.getClaimAsString("family_name");
                    String username = jwt.getClaimAsString("email");

                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setUsername(username);
                    newUser.setPassword("123456"); // Set a default password or generate a random one
                    newUser.setRoles(Collections.singleton("USER"));
                    userRepository.save(newUser);
                    System.out.println("New user saved to database.");
                } else {
                    System.out.println("User already exists in database.");
                }
            }
        } else {
            System.out.println("Authentication is not a JwtAuthenticationToken");
        }

        filterChain.doFilter(request, response);
    }
}
