package com.example.ecommercebackend.controller;

import com.example.ecommercebackend.dto.UserRequestDTO;
import com.example.ecommercebackend.dto.UserResponseDTO;
import com.example.ecommercebackend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserControllerMVC.class)
public class UserControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllUsers_WhenCalled_ReturnsListOfUsers() throws Exception {
        // Arrange
        List<UserResponseDTO> userList = new ArrayList<>();
        UserResponseDTO user1 = new UserResponseDTO();
        user1.setId(1L);
        user1.setFirstName("Alice");
        user1.setLastName("Smith");
        user1.setEmail("alice.smith@example.com");

        UserResponseDTO user2 = new UserResponseDTO();
        user2.setId(2L);
        user2.setFirstName("Bob");
        user2.setLastName("Johnson");
        user2.setEmail("bob.johnson@example.com");

        userList.add(user1);
        userList.add(user2);

        when(userService.getAllUsers()).thenReturn(userList);

        // Act and Assert
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(userList)));
    }

    @Test
    public void getUserById_WhenUserExists_ReturnsUser() throws Exception {
        // Arrange
        UserResponseDTO user = new UserResponseDTO();
        user.setId(1L);
        user.setFirstName("Alice");
        user.setLastName("Smith");
        user.setEmail("alice.smith@example.com");

        when(userService.getUserById(1L)).thenReturn(user);

        // Act and Assert
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(user)));
    }

    @Test
    public void createUser_WhenValidRequest_ReturnsCreatedUser() throws Exception {
        // Arrange
        UserRequestDTO userToCreate = new UserRequestDTO();
        userToCreate.setFirstName("Charlie");
        userToCreate.setLastName("Brown");
        userToCreate.setEmail("charlie.brown@example.com");

        UserResponseDTO expectedUser = new UserResponseDTO();
        expectedUser.setId(1L);
        expectedUser.setFirstName("Charlie");
        expectedUser.setLastName("Brown");
        expectedUser.setEmail("charlie.brown@example.com");

        when(userService.saveUser(any(UserRequestDTO.class))).thenReturn(expectedUser);

        // Act and Assert
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToCreate)))
                .andExpect(status().isCreated())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedUser)));
    }

    @Test
    public void updateUser_WhenUserExists_ReturnsUpdatedUser() throws Exception {
        // Arrange
        UserRequestDTO userToUpdate = new UserRequestDTO();
        userToUpdate.setFirstName("Alice Updated");
        userToUpdate.setLastName("Smith Updated");
        userToUpdate.setEmail("alice.updated@example.com");

        UserResponseDTO existingUser = new UserResponseDTO();
        existingUser.setId(1L);
        existingUser.setFirstName("Alice");
        existingUser.setLastName("Smith");
        existingUser.setEmail("alice.smith@example.com");

        UserResponseDTO updatedUser = new UserResponseDTO();
        updatedUser.setId(1L);
        updatedUser.setFirstName("Alice Updated");
        updatedUser.setLastName("Smith Updated");
        updatedUser.setEmail("alice.updated@example.com");

        when(userService.getUserById(1L)).thenReturn(existingUser);
        when(userService.saveUser(any(UserRequestDTO.class))).thenReturn(updatedUser);

        // Act and Assert
        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userToUpdate)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(updatedUser)));
    }

    @Test
    public void deleteUser_WhenUserExists_ReturnsStatusOk() throws Exception {
        // Arrange
        UserResponseDTO existingUser = new UserResponseDTO();
        existingUser.setId(1L);
        existingUser.setFirstName("Alice");
        existingUser.setLastName("Smith");
        existingUser.setEmail("alice.smith@example.com");

        when(userService.getUserById(1L)).thenReturn(existingUser);

        // Act and Assert
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void patchUser_WhenUserExists_ReturnsPatchedUser() throws Exception {
        // Arrange
        UserRequestDTO userPatch = new UserRequestDTO();
        userPatch.setFirstName("Alice Patched");

        UserResponseDTO existingUser = new UserResponseDTO();
        existingUser.setId(1L);
        existingUser.setFirstName("Alice");
        existingUser.setLastName("Smith");
        existingUser.setEmail("alice.smith@example.com");

        UserResponseDTO patchedUser = new UserResponseDTO();
        patchedUser.setId(1L);
        patchedUser.setFirstName("Alice Patched");
        patchedUser.setLastName("Smith");
        patchedUser.setEmail("alice.smith@example.com");

        when(userService.patchUser(1L, userPatch)).thenReturn(patchedUser);

        // Act and Assert
        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userPatch)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(patchedUser)));
    }
}
