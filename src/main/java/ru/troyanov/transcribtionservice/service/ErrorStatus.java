package ru.troyanov.transcribtionservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskDto;
import ru.troyanov.transcribtionservice.model.Status;

@Component
public class ErrorStatus implements StatusHandler {
    @Override
    public ResponseEntity<TaskDto> handle(String taskId) {
        return new ResponseEntity<>(TaskDto.builder()
                .taskId(taskId)
                .status(Status.ERROR)
                .taskResult("Error service")
                .build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
