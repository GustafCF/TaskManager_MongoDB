package com.br.task_mongoDB.service.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1l;

    public ResourceNotFoundException(String message){
        super(message);
    }

}
