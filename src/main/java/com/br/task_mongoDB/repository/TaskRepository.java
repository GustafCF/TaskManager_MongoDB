package com.br.task_mongoDB.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.br.task_mongoDB.domain.Task;

@Repository
public interface TaskRepository extends MongoRepository<Task, String>{
}
