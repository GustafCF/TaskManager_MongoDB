package com.br.task_mongoDB.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.task_mongoDB.domain.User;
import com.br.task_mongoDB.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findById(String id){
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(()-> new IllegalArgumentException("Id not found!"));
    }

    public User createUser(User user){
        return userRepository.save(user);
    }

    public void delete(String id){
        userRepository.deleteById(id);
    }

    public User update(String id, User entity){
        User obj = findById(id);
        updateData(obj, entity);
        return userRepository.save(obj);
    }

    public void updateData(User entity, User obj){
        entity.setName(obj.getName());
        entity.setEmail(obj.getEmail());
        entity.setPhone(obj.getPhone());
    }

}
