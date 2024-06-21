package com.example.ecommercebackend.service;

import com.example.ecommercebackend.models.User;
import com.example.ecommercebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user by username: " + username);

        User user = userRepository.findByUsername(username);
        System.out.println("User found: " + user);

        if (user == null) {
            System.out.println("User not found with username: " + username);
            throw new UsernameNotFoundException("User not found with username: " + username );
        }

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
        System.out.println("UserDetails: " + userDetails);

        return userDetails;
    }

    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        Collection<? extends GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        System.out.println("GrantedAuthorities: " + authorities);
        return authorities;
    }
}
