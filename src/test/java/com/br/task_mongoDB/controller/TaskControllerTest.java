package com.br.task_mongoDB.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.br.task_mongoDB.domain.Task;
import com.br.task_mongoDB.domain.dto.UserDTO;
import com.br.task_mongoDB.service.TaskService;

public class TaskControllerTest {
    
    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    // @Mock
    // private WebSocketController webSocketController;

    private Task task1;
    private Task task2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        task1 = new Task("Tarefa 1", "Descrição da tarefa 1", null, new UserDTO());
        task2 = new Task("Tarefa 2", "Descrição da tarefa 2", null, new UserDTO());
    }

    @Test
    void testListAll() {
        when(taskService.findAll()).thenReturn(Arrays.asList(task1, task2));
        ResponseEntity<List<Task>> response = taskController.listAll();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(taskService, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(taskService.findById(any(String.class))).thenReturn(task1);

        ResponseEntity<Task> response = taskController.findById("1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tarefa 1", response.getBody().getTitle());
        verify(taskService, times(1)).findById(any(String.class));
    }

    // @Test
    // void testCreateTask() {
    //     when(taskService.createTask(any(String.class), any(Task.class))).thenReturn(task1);

    //     URI fakeUri = URI.create("/task/create/1");
    //     ResponseEntity<Task> response = taskController.create("1", task1);

    //     assertNotNull(response);
    //     assertEquals(HttpStatus.CREATED, response.getStatusCode());
    //     assertEquals("Tarefa 1", response.getBody().getTitle());
        // verify(taskService, times(1)).createTask(any(String.class), any(Task.class));
        // verify(webSocketController, times(1)).broadcast(anyString());
    // }

    @Test
    void testDelete() {
        ResponseEntity<Void> response = taskController.delete("1");

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService, times(1)).delete(any(String.class));
    }

    @Test
    void testUpdate() {
        when(taskService.update(any(String.class), any(Task.class))).thenReturn(task1);
        ResponseEntity<Task> response = taskController.update("1", task1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tarefa 1", response.getBody().getTitle());
        verify(taskService, times(1)).update(any(String.class), any(Task.class));
    }
}