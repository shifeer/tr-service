package ru.troyanov.transcribtionservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/main")
    public String index() {
        return "main";
    }
    @GetMapping("/transcription")
    public String loading() {
        return "transcription";
    }
}
