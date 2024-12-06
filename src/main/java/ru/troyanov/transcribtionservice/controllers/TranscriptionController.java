package ru.troyanov.transcribtionservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.model.TaskTranscription;
import ru.troyanov.transcribtionservice.service.MultiToSimpleFileService;
import ru.troyanov.transcribtionservice.service.StatusTranscriptionHandlerService;
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
    private final StatusTranscriptionHandlerService statusTranscriptionHandlerService;
    private final MultiToSimpleFileService multiToSimpleFileService;

    @PostMapping
    public ResponseEntity<TaskTranscription> doTranscription(@ValidFileFormatOrEmpty @RequestParam("file") MultipartFile multipartFile) {

        String taskId = UUID.randomUUID().toString();
        transcriptionService.doTranscribe(multiToSimpleFileService.multiToFile(multipartFile), taskId);

        return statusTranscriptionHandlerService.getResponse(Status.PROCESSING, taskId);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskTranscription> getResult(@PathVariable("taskId") String taskId) {

        if (taskId == null) {
            log.warn("Task id is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return statusTranscriptionHandlerService.getResponse(taskId);
    }
}
