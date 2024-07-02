package com.example.userservice.controller;

import com.example.userservice.dto.UserRequestDTO;
import com.example.userservice.dto.UserResponseDTO;
import com.example.userservice.models.CustomUserDetails;
import com.example.userservice.producers.KafkaEmailProducer;
import com.example.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserControllerMVC {

    @Autowired
    private UserService userService;


    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id, @AuthenticationPrincipal UserDetails authenticatedUser, @RequestHeader("Authorization") String bearerToken) {
        UserResponseDTO user = userService.getUserById(id);
//        System.out.println("Token: " + bearerToken);
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        System.out.println("Authentication: " + authentication);
//        System.out.println("Authentication: " + authentication.getPrincipal());
//        System.out.println("Authentication: " + authentication.getDetails());
//        CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        String token = userDetails.getToken();
//        System.out.println("Token in controller: " + bearerToken);
        if (user != null) {
            if (!(user.getRoles().contains("ADMIN") || (user.getEmail().equals(authenticatedUser.getUsername())))) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserRequestDTO userRequestDTO) {
        UserResponseDTO createdUser = userService.saveUser(userRequestDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO, @AuthenticationPrincipal UserDetails authenticatedUser) {
        UserResponseDTO user = userService.getUserById(id);
        System.out.println("AuthenticatedUser: " + authenticatedUser.getUsername());
        if (user != null) {
            if (!(user.getRoles().contains("ADMIN") || (user.getEmail().equals(authenticatedUser.getUsername())))) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            userRequestDTO.setId(id);
            UserResponseDTO updatedUser = userService.saveUser(userRequestDTO);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id, @AuthenticationPrincipal UserDetails authenticatedUser) {
        UserResponseDTO user = userService.getUserById(id);
        if (user != null) {
            if (!(user.getRoles().contains("ADMIN") || (user.getEmail().equals(authenticatedUser.getUsername())))) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserResponseDTO> patchUser(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO, @AuthenticationPrincipal UserDetails authenticatedUser) {
        UserResponseDTO user = userService.getUserById(id);
        UserResponseDTO sample = new UserResponseDTO();
        sample.setUsername(authenticatedUser.getUsername());
        System.out.println("Authenticated user username: "+authenticatedUser.getUsername());
        if (user != null) {
            if (!(user.getRoles().contains("ADMIN") || (user.getEmail().equals(authenticatedUser.getUsername())))) {
                return new ResponseEntity<>(sample, HttpStatus.FORBIDDEN);
            }
            try {
                UserResponseDTO updatedUser = userService.patchUser(id, userRequestDTO);
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } catch (IllegalArgumentException e) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
