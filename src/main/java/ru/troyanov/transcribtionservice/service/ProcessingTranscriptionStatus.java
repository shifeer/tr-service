package ru.troyanov.transcribtionservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.model.TaskTranscription;

@Component
public class ProcessingTranscriptionStatus implements StatusProcessor<TaskTranscription> {
    @Override
    public ResponseEntity<TaskTranscription> handle(String taskId) {
        return new ResponseEntity<>(TaskTranscription.builder()
                .taskId(taskId)
                .status(Status.PROCESSING)
                .build(), HttpStatus.ACCEPTED);
    }
}
