package ru.troyanov.transcribtionservice.controllers;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.troyanov.transcribtionservice.configurations.StatusHandlerFactory;
import ru.troyanov.transcribtionservice.dto.TaskDto;
import ru.troyanov.transcribtionservice.exception.TaskNotFoundException;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;
import ru.troyanov.transcribtionservice.service.TranscriptionService;
import ru.troyanov.transcribtionservice.validators.ValidFileFormatOrEmpty;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/transcription")
@RequiredArgsConstructor
@Validated
public class TranscriptionController {

    private final TranscriptionService transcriptionService;
    private final RedisRepository redisRepository;
    private final StatusHandlerFactory statusHandlerFactory;

    @PostMapping
    public ResponseEntity<TaskDto> doTranscription(@ValidFileFormatOrEmpty @RequestParam("file") MultipartFile multipartFile) {

        String taskId = UUID.randomUUID().toString();

        redisRepository.createNewTask(taskId);
        transcriptionService.doTranscribe(multipartFile, taskId);

        return statusHandlerFactory.getResponse(Status.PROCESSING, taskId);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDto> getResult(@PathVariable("taskId") String taskId) {

        if (taskId == null) {
            log.warn("Task id is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Status status = Status.fromString(redisRepository.getTaskStatus(taskId));

        return statusHandlerFactory.getResponse(status, taskId);
    }
}
