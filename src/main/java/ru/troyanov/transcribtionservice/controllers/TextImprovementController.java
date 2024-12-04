package ru.troyanov.transcribtionservice.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.troyanov.transcribtionservice.dto.RequestImprTextDto;
import ru.troyanov.transcribtionservice.dto.ResponseImprTextDto;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.service.ImprovementTextService;
import ru.troyanov.transcribtionservice.service.StatusHandler;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/improvement")
public class TextImprovementController {

    private final ImprovementTextService improvementTextService;
    private final StatusHandler<ResponseImprTextDto> statusHandler;

    public TextImprovementController(ImprovementTextService improvementTextService, @Qualifier("statusImprovementTextHandlerService") StatusHandler<ResponseImprTextDto> statusHandler) {
        this.improvementTextService = improvementTextService;
        this.statusHandler = statusHandler;
    }

    @PostMapping
    public ResponseEntity<ResponseImprTextDto> getImprovementText(@RequestBody RequestImprTextDto imprTextDto) {

        String taskId = UUID.randomUUID().toString();
        improvementTextService.improvementText(imprTextDto, taskId);

        return statusHandler.getResponse(Status.PROCESSING, taskId);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<ResponseImprTextDto> getImprovementText(@PathVariable String taskId) {
        if (taskId == null) {
            log.warn("Task id is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return statusHandler.getResponse(taskId);
    }
}

