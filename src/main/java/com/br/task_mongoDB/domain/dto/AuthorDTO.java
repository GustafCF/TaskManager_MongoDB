package com.br.task_mongoDB.domain.dto;

import java.io.Serial;
import java.io.Serializable;

import com.br.task_mongoDB.domain.User;

public class AuthorDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    String id;
    String name;

    public AuthorDTO(){}

    public AuthorDTO(User obj){
        id = obj.getId();
        name = obj.getName();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}
