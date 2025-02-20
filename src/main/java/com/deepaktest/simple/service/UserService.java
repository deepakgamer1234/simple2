package com.deepaktest.simple.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.deepaktest.simple.entity.User;
import com.deepaktest.simple.entity.UserDTO;
import com.deepaktest.simple.exception.UserNotFoundException;
import com.deepaktest.simple.repository.UserRepository;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users with isDelete set to false."); //LOger in service layer
        List<User> byIsDeleteFalse = userRepository.findByIsDeleteFalse();
        return byIsDeleteFalse.stream()
                .map(user -> new UserDTO(user.getId(), user.getName(), user.getEmail()))
                .toList();
    }

    public Optional<UserDTO> getUserById(Long id) {
        logger.info("Fetching user with id: {}", id);
        Optional<User> byId = userRepository.findById(id);
        return byId.filter(user -> !user.isDelete())
                   .map(u -> new UserDTO(u.getId(), u.getName(), u.getEmail()));
    }

    public User createUser(User user) {
        logger.info("Creating a new user with email: {}", user.getEmail());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        logger.debug("User created with id: {}", savedUser.getId());
        return savedUser;
    }

    public User updateUser(Long id, User user) {
        logger.info("Updating user with id: {}", id);
        if (userRepository.existsById(id)) {
            user.setId(id);
            User updatedUser = userRepository.save(user);
            logger.debug("User updated with id: {}", updatedUser.getId());
            return updatedUser;
        } else {
            logger.error("User with id {} not found", id);
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }

    public void deleteUser(Long id) {
        logger.info("Attempting to delete user with id: {}", id);
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setDelete(true);
            userRepository.save(user);
            logger.debug("User with id {} marked as deleted.", id);
        } else {
            logger.error("User with id {} not found", id);
            throw new UserNotFoundException("User with id " + id + " not found");
        }
    }
}
