package com.example.ecommercebackend.dto;


import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserResponseDTO {

    public Long id;
    public String username;
    public String email;
    public String firstName;
    public String lastName;

}
