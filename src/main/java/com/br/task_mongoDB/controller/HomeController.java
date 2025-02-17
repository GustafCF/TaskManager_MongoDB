package com.br.task_mongoDB.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/htmluser")
    public String userhtml(){
        return "user";
    }

    @GetMapping("/htmltask")
    public String taskhtml(){
        return "task";
    }
}