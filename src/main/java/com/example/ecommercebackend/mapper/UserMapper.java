package com.example.ecommercebackend.mapper;

import com.example.ecommercebackend.dto.UserRequestDTO;
import com.example.ecommercebackend.dto.UserResponseDTO;
import com.example.ecommercebackend.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static User getUserFromRequestDTO(UserRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return user;
    }

    public static UserResponseDTO getUserResponseDTOFromUser(User user) {
        if (user == null) {
            return null;
        }

        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setUsername(user.getUsername());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setFirstName(user.getFirstName());
        responseDTO.setLastName(user.getLastName());
        return responseDTO;
    }

    public static List<UserResponseDTO> getUserResponseDTOListFromUsers(List<User> users) {
        List<UserResponseDTO> responseDTOS = new ArrayList<>();
        for (User user : users) {
            responseDTOS.add(getUserResponseDTOFromUser(user));
        }
        return responseDTOS;
    }
}