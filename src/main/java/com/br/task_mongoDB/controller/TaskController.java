package com.br.task_mongoDB.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.br.task_mongoDB.domain.Task;
import com.br.task_mongoDB.service.TaskService;

@RestController
@RequestMapping(value = "/task")
public class TaskController {

    private final WebSocketController webSocketController; 
    private final TaskService service;

    public TaskController(WebSocketController webSocketController, TaskService service){
        this.webSocketController = webSocketController;
        this.service = service;
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<Task>> listAll(){
        List<Task> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<Task> findById(@PathVariable String id){
        Task task = service.findById(id);
        return ResponseEntity.ok().body(task); 
    }

    @PostMapping(value = "/create/{id}")
    public ResponseEntity<Task> create(@PathVariable String id, @RequestBody Task task){
        var obj = service.createTask(id, task);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        webSocketController.broadcast("Nova tarefa criado: " + obj.getTitle());
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<Task> update(@PathVariable String id, @RequestBody Task entity){
        Task task = service.update(id, entity);
        return ResponseEntity.ok().body(task);
    }

}
