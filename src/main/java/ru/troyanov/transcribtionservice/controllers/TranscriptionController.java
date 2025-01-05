package ru.troyanov.transcribtionservice.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.troyanov.transcribtionservice.dto.Language;
import ru.troyanov.transcribtionservice.dto.Status;
import ru.troyanov.transcribtionservice.dto.TaskTranscriptionDTO;
import ru.troyanov.transcribtionservice.service.StatusTranscriptionHandlerService;
import ru.troyanov.transcribtionservice.service.TranscriptionService;
import ru.troyanov.transcribtionservice.validators.ValidFileFormatOrEmpty;

import java.io.File;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.write;

@Tag(name = "TranscriptionController")
@Slf4j
@RestController
@RequestMapping("/api/v1/transcription")
@RequiredArgsConstructor
@Validated
public class TranscriptionController {

    private final TranscriptionService transcriptionService;
    private final StatusTranscriptionHandlerService statusTranscriptionHandlerService;
    @Value("${path.to.dir}")
    private Path pathToDir;

    @PostMapping
    public ResponseEntity<TaskTranscriptionDTO> doTranscription(@ValidFileFormatOrEmpty @RequestParam("file") MultipartFile multipartFile,
                                                                @RequestParam("language") String lang) {

        Optional<Language> language = Language.fromValue(lang);
        if (language.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        String taskId = UUID.randomUUID().toString();
        transcriptionService.doTranscribe(multiToFile(multipartFile), taskId, language.get());

        return statusTranscriptionHandlerService.getResponse(Status.PROCESSING, taskId);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskTranscriptionDTO> getResult(@PathVariable("taskId") String taskId) {

        if (taskId == null) {
            log.warn("Task id is empty");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return statusTranscriptionHandlerService.getResponse(taskId);
    }

    @SneakyThrows
    private File multiToFile(MultipartFile multipartFile) {

        byte[] audioBytes = multipartFile.getBytes();
        Path file = createTempFile(Path.of(System.getProperty("user.dir") + pathToDir), multipartFile.getOriginalFilename(), "");
        write(file, audioBytes);

        return file.toFile();
    }
}
