package com.br.task_mongoDB.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import com.br.task_mongoDB.domain.Task;
import com.br.task_mongoDB.domain.User;
import com.br.task_mongoDB.domain.dto.UserDTO;
import com.br.task_mongoDB.repository.TaskRepository;
import com.br.task_mongoDB.repository.UserRepository;
import com.br.task_mongoDB.service.exceptions.ResourceNotFoundException;

public class TaskServiceTest {

     @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    private Task task1;
    private Task task2;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        user = new User("Alice", "alice@example.com", 123456);
        user.setId("1");

        task1 = new Task("Task 1", "Description 1", LocalDateTime.now(), new UserDTO(user));
        task1.setId("101");

        task2 = new Task("Task 2", "Description 2", LocalDateTime.now(), new UserDTO(user));
        task2.setId("102");
    }

    @Test
    void testFindAll() {
        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2));

        List<Task> tasks = taskService.findAll();

        assertNotNull(tasks);
        assertEquals(2, tasks.size());
        assertEquals("Task 1", tasks.get(0).getTitle());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    void testFindById_TaskExists() {
        when(taskRepository.findById("101")).thenReturn(Optional.of(task1));

        Task foundTask = taskService.findById("101");

        assertNotNull(foundTask);
        assertEquals("Task 1", foundTask.getTitle());
        verify(taskRepository, times(1)).findById("101");
    }

    @Test
    void testFindById_TaskNotFound() {
        when(taskRepository.findById("999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskService.findById("999");
        });

        assertEquals("Id not found", exception.getMessage());
        verify(taskRepository, times(1)).findById("999");
    }

    @Test
    void testCreateTask() {
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task1);
        when(userRepository.save(any(User.class))).thenReturn(user);

        Task createdTask = taskService.createTask("1", task1);

        assertNotNull(createdTask);
        assertEquals("Task 1", createdTask.getTitle());
        assertEquals("Alice", createdTask.getUser().getName());

        verify(userRepository, times(1)).findById("1");
        verify(taskRepository, times(1)).save(any(Task.class));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateTask_UserNotFound() {
        when(userRepository.findById("99")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskService.createTask("99", task1);
        });

        assertEquals("99", exception.getMessage());
        verify(userRepository, times(1)).findById("99");
    }

    @Test
    void testDelete_TaskExists() {
        doNothing().when(taskRepository).deleteById("101");

        assertDoesNotThrow(() -> taskService.delete("101"));
        verify(taskRepository, times(1)).deleteById("101");
    }

    @Test
    void testDelete_TaskNotFound() {
        doThrow(new EmptyResultDataAccessException(1)).when(taskRepository).deleteById("999");

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskService.delete("999");
        });

        assertEquals("Id not found", exception.getMessage());
        verify(taskRepository, times(1)).deleteById("999");
    }

    @Test
    void testDelete_DatabaseException() {
        doThrow(new DataIntegrityViolationException("Integrity violation")).when(taskRepository).deleteById("101");

        Exception exception = assertThrows(com.br.task_mongoDB.service.exceptions.DatabaseException.class, () -> {
            taskService.delete("101");
        });

        assertTrue(exception.getMessage().contains("Integrity violation"));
        verify(taskRepository, times(1)).deleteById("101");
    }

    @Test
    void testUpdate_TaskExists() {
        Task updatedTask = new Task("Updated Task", "Updated Description", LocalDateTime.now(), new UserDTO(user));
        updatedTask.setId("101");

        when(taskRepository.findById("101")).thenReturn(Optional.of(task1));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        Task result = taskService.update("101", updatedTask);

        assertNotNull(result);
        assertEquals("Updated Task", result.getTitle());
        assertEquals("Updated Description", result.getDescription());

        verify(taskRepository, times(1)).findById("101");
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void testUpdate_TaskNotFound() {
        when(taskRepository.findById("999")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            taskService.update("999", task1);
        });

        assertEquals("Id not found", exception.getMessage());
        verify(taskRepository, times(1)).findById("999");
    }
}
