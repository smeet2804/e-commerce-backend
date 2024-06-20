package com.example.ecommercebackend.dto;


import lombok.Data;
import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Data
@Getter
public class UserResponseDTO {

    public Long id;
    public String username;
    public String email;
    public String firstName;
    public String lastName;
    public Set<String> roles;

}
