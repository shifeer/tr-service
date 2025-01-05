package ru.troyanov.transcribtionservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.Status;
import ru.troyanov.transcribtionservice.dto.TaskTranscriptionDTO;

@Component
public class ProcessingTranscriptionStatus implements StatusProcessor<TaskTranscriptionDTO> {
    @Override
    public ResponseEntity<TaskTranscriptionDTO> handle(String taskId) {
        return new ResponseEntity<>(TaskTranscriptionDTO.builder()
                .taskId(taskId)
                .status(Status.PROCESSING)
                .build(), HttpStatus.ACCEPTED);
    }
}
