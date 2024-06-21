package com.example.ecommercebackend.config;

import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Component
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public CustomJwtAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("CustomJwtAuthenticationFilter: Request received");

        // Extract access token from the Authorization header
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);
            System.out.println("Access token: " + accessToken);

            // Fetch user info from Google using access token
            RestTemplate restTemplate = new RestTemplate();
            String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";
            Map<String, Object> userInfo = restTemplate.getForObject(userInfoEndpoint + "?access_token=" + accessToken, Map.class);
            System.out.println("User info: " + userInfo);

            if (userInfo != null) {
                String email = (String) userInfo.get("email");
                System.out.println("User email: " + email);

                if (email != null) {
                    User existingUser = userRepository.findByEmail(email);
                    if (existingUser == null) {
                        System.out.println("User not found in database. Creating new user.");

                        String firstName = (String) userInfo.get("given_name");
                        String lastName = (String) userInfo.get("family_name");
                        String username = email;

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
            }
        }

        filterChain.doFilter(request, response);
    }
}
