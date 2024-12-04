package ru.troyanov.transcribtionservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskTranscriptionDto;
import ru.troyanov.transcribtionservice.model.Status;

@Component
public class ProcessingTranscriptionStatus implements StatusProcessor<TaskTranscriptionDto> {
    @Override
    public ResponseEntity<TaskTranscriptionDto> handle(String taskId) {
        return new ResponseEntity<>(TaskTranscriptionDto.builder()
                .taskId(taskId)
                .status(Status.PROCESSING)
                .build(), HttpStatus.ACCEPTED);
    }
}
