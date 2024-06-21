package com.example.userservice.service;

import com.example.userservice.dto.UserRequestDTO;
import com.example.userservice.dto.UserResponseDTO;

import java.util.List;

public interface IUserService {

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long id);

    UserResponseDTO saveUser(UserRequestDTO userRequestDTO);

    void deleteUser(Long id);

    UserResponseDTO patchUser(Long id, UserRequestDTO userRequestDTO);
}
