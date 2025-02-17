package com.br.task_mongoDB.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.br.task_mongoDB.domain.User;
import com.br.task_mongoDB.repository.UserRepository;

public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user1 = new User("Alice", "alice@example.com", 123456);
        user1.setId("1");

        user2 = new User("Bob", "bob@example.com", 654321);
        user2.setId("2");
    }

    @Test
    void testFindAll() {
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.findAll();

        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals("Alice", users.get(0).getName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindById_UserExists() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user1));

        User foundUser = userService.findById("1");

        assertNotNull(foundUser);
        assertEquals("Alice", foundUser.getName());
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    void testFindById_UserNotFound() {
        when(userRepository.findById("3")).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.findById("3");
        });

        assertEquals("Id not found!", exception.getMessage());
        verify(userRepository, times(1)).findById("3");
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(user1)).thenReturn(user1);

        User createdUser = userService.createUser(user1);

        assertNotNull(createdUser);
        assertEquals("Alice", createdUser.getName());
        verify(userRepository, times(1)).save(user1);
    }

    @Test
    void testDelete() {
        doNothing().when(userRepository).deleteById("1");

        assertDoesNotThrow(() -> userService.delete("1"));
        verify(userRepository, times(1)).deleteById("1");
    }

    @Test
    void testUpdate() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user1));
        when(userRepository.save(any(User.class))).thenReturn(user1);

        User updatedUser = new User("Alice Updated", "alice.new@example.com", 789101);
        User result = userService.update("1", updatedUser);

        assertNotNull(result);
        assertEquals("Alice Updated", result.getName());
        assertEquals("alice.new@example.com", result.getEmail());
        assertEquals(789101, result.getPhone());

        verify(userRepository, times(1)).findById("1");
        verify(userRepository, times(1)).save(any(User.class));
    }
}