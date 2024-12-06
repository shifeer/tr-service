package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.exception.TaskExecuteException;
import ru.troyanov.transcribtionservice.model.TaskTranscription;

@Component
public class ErrorTranscriptionStatus implements StatusProcessor<TaskTranscription> {
    @Override
    public ResponseEntity<TaskTranscription> handle(String taskId) {
        throw new TaskExecuteException("Error when working with the task");
    }
}
