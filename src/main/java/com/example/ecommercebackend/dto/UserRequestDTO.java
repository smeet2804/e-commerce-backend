package com.example.ecommercebackend.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequestDTO {

    public Long id;
    public String username;
    public String password;
    public String email;
    public String firstName;
    public String lastName;
}