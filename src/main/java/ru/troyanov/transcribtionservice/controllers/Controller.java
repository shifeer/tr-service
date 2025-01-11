package ru.troyanov.transcribtionservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping
    public String index() {
        return "main";
    }
    @GetMapping("/transcription")
    public String loading(@RequestParam(name = "taskId", required = false) String taskId) {
        return "transcription";
    }
    @GetMapping("/socket")
    public String socket() {
        return "socket";
    }
}
