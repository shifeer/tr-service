package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.exception.TaskExecuteException;
import ru.troyanov.transcribtionservice.dto.TaskImprovementDTO;

@Component
public class ErrorImprovStatus implements StatusProcessor<TaskImprovementDTO> {
    @Override
    public ResponseEntity<TaskImprovementDTO> handle(String taskId) {
        throw new TaskExecuteException("Error when working with the task");
    }
}
