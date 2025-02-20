package com.deepaktest.simple;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.deepaktest.simple.entity.User;
import com.deepaktest.simple.entity.UserDTO;
import com.deepaktest.simple.repository.UserRepository;
import com.deepaktest.simple.service.UserService;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder; // Mock PasswordEncoder

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers_Success() {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId(1L);
        user1.setName("param");
        user1.setEmail("param@example.com");
        user1.setDelete(false);

        User user2 = new User();
        user2.setId(2L);
        user2.setName("abhi");
        user2.setEmail("abhi@example.com");
        user2.setDelete(false);

        userList.add(user1);
        userList.add(user2);

        when(userRepository.findByIsDeleteFalse()).thenReturn(userList);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(2, result.size());
        assertEquals("param", result.get(0).getName());
        assertEquals("abhi", result.get(1).getName());
    }

    @Test
    void testGetAllUsers_NoUsers() {
        when(userRepository.findByIsDeleteFalse()).thenReturn(new ArrayList<>());

        List<UserDTO> result = userService.getAllUsers();

        assertTrue(result.isEmpty());
    }

    @Test
    void testGetUserById_UserExistsAndIsNotDeleted() {
        User user = new User();
        user.setId(1L);
        user.setName("qwe");
        user.setEmail("qwe@example.com");
        user.setDelete(false);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.getUserById(1L);

        assertTrue(result.isPresent());
        assertEquals("qwe", result.get().getName());
        assertEquals("qwe@example.com", result.get().getEmail());
    }

    @Test
    void testGetUserById_UserExistsAndIsDeleted() {
        User user = new User();
        user.setId(1L);
        user.setName("qwe");
        user.setEmail("qwe@example.com");
        user.setDelete(true);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<UserDTO> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetUserById_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserDTO> result = userService.getUserById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testCreateUser_Success() {
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        user.setPassword("rawPassword");

        when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.createUser(user);

        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword()); // Assert encoded password
    }
}
