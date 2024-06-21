package com.example.ecommercebackend.filters;

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
public class CustomOpaqueTokenAuthenticationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;

    public CustomOpaqueTokenAuthenticationFilter(UserRepository userRepository) {
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
                        handleUserAuthentication(email, userInfo, request);
                    }
                }
            } catch (HttpClientErrorException e) {
                handleUnauthorizedException(response, e);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleUserAuthentication(String email, Map<String, Object> userInfo, HttpServletRequest request) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            user = createUser(userInfo, email);
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

    private User createUser(Map<String, Object> userInfo, String email) {
        String firstName = (String) userInfo.get("given_name");
        String lastName = (String) userInfo.get("family_name");

        User user = new User();
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(email);
        user.setPassword("123456"); // Ideally, set a more secure default password or use OAuth2 only
        user.setRoles(Collections.singleton(email.equals("shahsmeet2804@gmail.com") ? "ADMIN" : "USER"));
        userRepository.save(user);
        return user;
    }

    private void handleUnauthorizedException(HttpServletResponse response, HttpClientErrorException e) throws IOException {
        if (e.getStatusCode().value() == HttpServletResponse.SC_UNAUTHORIZED) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"invalid_request\", \"error_description\": \"Invalid Credentials\"}");
        } else {
            throw e;
        }
    }
}
