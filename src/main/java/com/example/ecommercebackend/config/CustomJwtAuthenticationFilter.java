package com.example.ecommercebackend.config;

import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public CustomJwtAuthenticationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String accessToken = authorizationHeader.substring(7);

            try {
                RestTemplate restTemplate = new RestTemplate();
                String userInfoEndpoint = "https://www.googleapis.com/oauth2/v3/userinfo";
                Map<String, Object> userInfo = restTemplate.getForObject(userInfoEndpoint + "?access_token=" + accessToken, Map.class);

                if (userInfo != null) {
                    String email = (String) userInfo.get("email");

                    if (email != null) {
                        User user = userRepository.findByEmail(email);
                        if (user == null) {
                            String firstName = (String) userInfo.get("given_name");
                            String lastName = (String) userInfo.get("family_name");

                            user = new User();
                            user.setEmail(email);
                            user.setFirstName(firstName);
                            user.setLastName(lastName);
                            user.setUsername(email);
                            user.setPassword("123456"); // Ideally, set a more secure default password or use OAuth2 only
                            if (email.equals("shahsmeet2804@gmail.com")) {
                                user.setRoles(Collections.singleton("ADMIN"));
                            }
                            else {
                                user.setRoles(Collections.singleton("USER")); // Default role USER
                            }
                            userRepository.save(user);
                        }

                        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                                user.getUsername(),
                                user.getPassword(),
                                user.getRoles().stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(Collectors.toList())
                        );
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode().value() == HttpServletResponse.SC_UNAUTHORIZED) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.getWriter().write("{\"error\": \"invalid_request\", \"error_description\": \"Invalid Credentials\"}");
                    return;
                } else {
                    throw e; // rethrow other exceptions
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
