package ru.troyanov.transcribtionservice.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
public class Controller {
    @GetMapping("/main")
    public String index() {
        return "main";
    }
    @GetMapping("/transcription")
    public String loading(@RequestParam(name = "taskId", required = false) String taskId) {

        return "transcription";
    }
}
