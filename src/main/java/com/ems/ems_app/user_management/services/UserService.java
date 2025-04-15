package com.ems.ems_app.user_management.services;


import com.ems.ems_app.user_management.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.ems.ems_app.user_management.dto.requestDTO.UserRequestDTO;
import com.ems.ems_app.user_management.dto.responseDTO.UserResponseDTO;
import com.ems.ems_app.user_management.entities.User;
import com.ems.ems_app.user_management.exception.DuplicateEmailException;
import com.ems.ems_app.user_management.exception.UserNotFoundException;


import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        // Check if email already exists
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists: " + userRequestDTO.getEmail());
        }

        User user = UserRequestDTO.convertToUserEntity(userRequestDTO, userRequestDTO.getUserType());
        User savedUser = userRepository.save(user);
        return UserResponseDTO.convertToUserResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        return UserResponseDTO.convertToUserResponseDTO(user);
    }

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserResponseDTO::convertToUserResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO updateUser(UUID userId, UserRequestDTO userRequestDTO) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        // Check if email is being updated and if the new email already exists for another user
        if (!existingUser.getEmail().equals(userRequestDTO.getEmail()) &&
                userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()) {
            throw new DuplicateEmailException("Email already exists: " + userRequestDTO.getEmail());
        }

        // Update fields
        existingUser.setFirstName(userRequestDTO.getFirstName());
        existingUser.setLastName(userRequestDTO.getLastName());
        existingUser.setEmail(userRequestDTO.getEmail());
        existingUser.setPassword(userRequestDTO.getPassword()); // Consider password hashing
        existingUser.setUserType(userRequestDTO.getUserType());

        User updatedUser = userRepository.update(existingUser);
        return UserResponseDTO.convertToUserResponseDTO(updatedUser);
    }

    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }
}

