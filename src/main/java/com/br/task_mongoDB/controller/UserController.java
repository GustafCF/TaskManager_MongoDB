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

import com.br.task_mongoDB.domain.User;
import com.br.task_mongoDB.service.UserService;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    private final WebSocketController webSocketController;
    private final UserService service;

    public UserController(WebSocketController webSocketController, UserService service){
        this.webSocketController = webSocketController;
        this.service = service;
    }

    @GetMapping(value = "/list")
    public ResponseEntity<List<User>> listAll(){
        List<User> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/find/{id}")
    public ResponseEntity<User> findById(@PathVariable String id){
        User User = service.findById(id);
        return ResponseEntity.ok().body(User); 
    }

    @PostMapping(value = "/create")
    public ResponseEntity<User> create(@RequestBody User User) {
        var obj = service.createUser(User);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        webSocketController.broadcast("Novo usuário criado: " + obj.getName());
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(value = "/update/{id}")
    public ResponseEntity<User> update(@PathVariable String id, @RequestBody User entity){
        User User = service.update(id, entity);
        return ResponseEntity.ok().body(User);
    }

}
