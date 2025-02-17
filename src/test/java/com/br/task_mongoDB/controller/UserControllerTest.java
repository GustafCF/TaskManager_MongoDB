package com.br.task_mongoDB.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import com.br.task_mongoDB.domain.User;
import com.br.task_mongoDB.service.UserService;

public class UserControllerTest {
    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private WebSocketController webSocketController;

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
    void testListAll() {
        when(userService.findAll()).thenReturn(Arrays.asList(user1, user2));

        ResponseEntity<List<User>> response = userController.listAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).findAll();
    }

    @Test
    void testFindById_UserExists() {
        when(userService.findById("1")).thenReturn(user1);

        ResponseEntity<User> response = userController.findById("1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alice", response.getBody().getName());
        verify(userService, times(1)).findById("1");
    }

    @Test
    void testFindById_UserNotFound() {
        when(userService.findById("99")).thenReturn(null);

        ResponseEntity<User> response = userController.findById("99");

        assertNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).findById("99");
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userService).delete("1");

        ResponseEntity<Void> response = userController.delete("1");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).delete("1");
    }

    @Test
    void testUpdateUser() {
        when(userService.update(eq("1"), any(User.class))).thenReturn(user1);

        ResponseEntity<User> response = userController.update("1", user1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Alice", response.getBody().getName());
        verify(userService, times(1)).update(eq("1"), any(User.class));
    }
}