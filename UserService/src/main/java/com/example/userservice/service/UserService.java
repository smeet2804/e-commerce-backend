package com.example.userservice.service;


import com.example.userservice.dto.UserRequestDTO;
import com.example.userservice.dto.UserResponseDTO;
import com.example.userservice.mapper.UserMapper;
import com.example.userservice.models.User;
import com.example.userservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
public class UserService implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return UserMapper.getUserResponseDTOListFromUsers(users);
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return UserMapper.getUserResponseDTOFromUser(user);
    }

    @Override
    public UserResponseDTO saveUser(UserRequestDTO userRequestDTO) {
        User user = new User();
        user.setUsername(userRequestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userRequestDTO.getPassword()));
        user.setEmail(userRequestDTO.getEmail());
        user.setFirstName(userRequestDTO.getFirstName());
        user.setLastName(userRequestDTO.getLastName());
        user.setRoles(Collections.singleton("USER"));
        User savedUser = userRepository.save(user);
        return UserMapper.getUserResponseDTOFromUser(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserResponseDTO patchUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        if (userRequestDTO.getUsername() != null) {
            user.setUsername(userRequestDTO.getUsername());
        }
        if (userRequestDTO.getPassword() != null) {
            user.setPassword(userRequestDTO.getPassword());
        }
        if (userRequestDTO.getEmail() != null) {
            user.setEmail(userRequestDTO.getEmail());
        }
        if (userRequestDTO.getFirstName() != null) {
            user.setFirstName(userRequestDTO.getFirstName());
        }
        if (userRequestDTO.getLastName() != null) {
            user.setLastName(userRequestDTO.getLastName());
        }
        userRepository.save(user);
        return UserMapper.getUserResponseDTOFromUser(user);
    }


    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void save(User user) {
        userRepository.save(user);
    }
}