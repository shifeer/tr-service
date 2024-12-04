package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskImprovementDto;
import ru.troyanov.transcribtionservice.exception.TaskExecuteException;

@Component
public class ErrorImprovStatus implements StatusProcessor<TaskImprovementDto> {
    @Override
    public ResponseEntity<TaskImprovementDto> handle(String taskId) {
        throw new TaskExecuteException("Error when working with the task");
    }
}
