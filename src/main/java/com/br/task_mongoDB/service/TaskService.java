package com.br.task_mongoDB.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.br.task_mongoDB.domain.Task;
import com.br.task_mongoDB.domain.User;
import com.br.task_mongoDB.domain.dto.UserDTO;
import com.br.task_mongoDB.repository.TaskRepository;
import com.br.task_mongoDB.repository.UserRepository;
import com.br.task_mongoDB.service.exceptions.DatabaseException;
import com.br.task_mongoDB.service.exceptions.ResourceNotFoundException;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository){
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }
    

    public List<Task> findAll(){
        return taskRepository.findAll();
    }

    public Task findById(String id){
        Optional<Task> task = taskRepository.findById(id);
        return task.orElseThrow(()-> new ResourceNotFoundException("Id not found"));
    }

    public Task createTask(String id, Task task) {
        User user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException(id));
        Task t1 = new Task();
        t1.setTitle(task.getTitle());
        t1.setDescription(task.getDescription());
        t1.setDueDate(LocalDateTime.now());
        t1.setUser(new UserDTO(user));
        taskRepository.save(t1);
        user.getTaskList().add(t1);
        userRepository.save(user);
        return t1;
    }
    
    public void delete(String id){
        try{
            taskRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found");
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    public Task update(String id, Task entity){
        try {
            Task task = findById(id);
            updateData(task, entity);
            return taskRepository.save(task);
        } catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found");
        }
    }

    private void updateData(Task entity, Task obj){
        if (obj.getTitle() != null || obj.getTitle().isEmpty()) {
            entity.setTitle(obj.getTitle());
        }
        if(obj.getDescription() != null || obj.getTitle().isEmpty()){
            entity.setDescription(obj.getDescription());
        }
        entity.setDueDate(LocalDateTime.now());
    }
}
