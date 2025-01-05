package ru.troyanov.transcribtionservice.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.troyanov.transcribtionservice.dto.RequestTaskImprovementDto;
import ru.troyanov.transcribtionservice.dto.Status;
import ru.troyanov.transcribtionservice.dto.TaskImprovementDTO;
import ru.troyanov.transcribtionservice.service.ImprovementTextService;
import ru.troyanov.transcribtionservice.service.StatusHandler;

import java.util.UUID;

@Tag(name = "ImprovementController")
@Slf4j
@RestController
@RequestMapping("/api/improvement")
public class ImprovementController {

    private final ImprovementTextService improvementTextService;
    private final StatusHandler<TaskImprovementDTO> statusHandler;

    public ImprovementController(ImprovementTextService improvementTextService, @Qualifier("statusImprovementHandlerService") StatusHandler<TaskImprovementDTO> statusHandler) {
        this.improvementTextService = improvementTextService;
        this.statusHandler = statusHandler;
    }

    @PostMapping
    public ResponseEntity<TaskImprovementDTO> getImprovementText(@RequestBody RequestTaskImprovementDto imprTextDto) {

        String taskId = UUID.randomUUID().toString();
        improvementTextService.improvementText(imprTextDto, taskId);

        return statusHandler.getResponse(Status.PROCESSING, taskId);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskImprovementDTO> getImprovementText(@PathVariable String taskId) {
        if (taskId == null) {
            log.warn("Task id is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return statusHandler.getResponse(taskId);
    }
}
