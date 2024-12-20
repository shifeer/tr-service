package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskDto;
import ru.troyanov.transcribtionservice.exception.TaskExecuteException;

@Component
public class ErrorStatus implements StatusProcessor<TaskDto> {
    @Override
    public ResponseEntity<TaskDto> handle(String taskId) {
        throw new TaskExecuteException("Error when working with the task");
    }
}
