package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskTranscriptionDto;
import ru.troyanov.transcribtionservice.exception.TaskExecuteException;

@Component
public class ErrorStatus implements StatusProcessor<TaskTranscriptionDto> {
    @Override
    public ResponseEntity<TaskTranscriptionDto> handle(String taskId) {
        throw new TaskExecuteException("Error when working with the task");
    }
}
