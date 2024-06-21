package com.example.ecommercebackend.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final JwtGrantedAuthoritiesConverter defaultGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        System.out.println("Converting JWT to GrantedAuthorities");

        Collection<GrantedAuthority> authorities = new ArrayList<>(defaultGrantedAuthoritiesConverter.convert(jwt));

        List<String> roles = jwt.getClaimAsStringList("roles");
        System.out.println("JWT roles claim: " + roles);

        if (roles != null) {
            Collection<GrantedAuthority> customAuthorities = roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                    .collect(Collectors.toList());
            authorities.addAll(customAuthorities);
        }

        System.out.println("GrantedAuthorities: " + authorities);
        return authorities;
    }
}
