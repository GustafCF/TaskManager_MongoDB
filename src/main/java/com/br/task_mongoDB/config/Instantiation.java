package com.br.task_mongoDB.config;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.br.task_mongoDB.domain.Task;
import com.br.task_mongoDB.domain.User;
import com.br.task_mongoDB.domain.dto.AuthorDTO;
import com.br.task_mongoDB.domain.dto.CommentDTO;
import com.br.task_mongoDB.domain.dto.UserDTO;
import com.br.task_mongoDB.repository.TaskRepository;
import com.br.task_mongoDB.repository.UserRepository;

@Configuration
public class Instantiation implements CommandLineRunner {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        
        taskRepository.deleteAll();
        userRepository.deleteAll();

        User u1 = new User("Gustavo", "gustavo@email.com", 912345678);
        User u2 = new User("Mariana", "mariana@email.com", 912345678);
        User u3 = new User("Verônica", "veronica@email.com", 912345678);

        userRepository.saveAll(Arrays.asList(u1, u2, u3));

        Task t1 = new Task("Mercado", "Fazer as compras do mês", LocalDateTime.now(), new UserDTO(u1));
        Task t2 = new Task("Farmácia", "Comprar pomada e lenço", LocalDateTime.now(), new UserDTO(u1));
        Task t3 = new Task("Remédio", "Dar remédio da Verônica", LocalDateTime.now(), new UserDTO(u2));

        taskRepository.saveAll(Arrays.asList(t1, t2, t3));

        CommentDTO c1 = new CommentDTO("Não esquecer de trazer o azeite", Instant.now(), new AuthorDTO(u2));
        CommentDTO c2 = new CommentDTO("O remédio deve ser dado a cada 6 horas de três em três gotas", Instant.now(), new AuthorDTO(u1));

        t1.getComments().add(c1);
        t3.getComments().add(c2);

        taskRepository.saveAll(Arrays.asList(t1, t3));
        
        u1.getTaskList().addAll(Arrays.asList(t1, t2));
        u2.getTaskList().add(t3);

        userRepository.saveAll(Arrays.asList(u1, u2));
    }

}
