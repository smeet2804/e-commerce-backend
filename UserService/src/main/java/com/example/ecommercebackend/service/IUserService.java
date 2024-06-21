package com.example.ecommercebackend.service;

import com.example.ecommercebackend.dto.UserRequestDTO;
import com.example.ecommercebackend.dto.UserResponseDTO;

import java.util.List;

public interface IUserService {

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO saveUser(UserRequestDTO userRequestDTO);

    void deleteUser(Long id);

    UserResponseDTO patchUser(Long id, UserRequestDTO userRequestDTO);
}
