package ru.troyanov.transcribtionservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/index")
    public String index() {
        return "index";
    }
}
