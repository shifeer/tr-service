package ru.troyanov.transcribtionservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskDto;
import ru.troyanov.transcribtionservice.model.Status;

@Component
public class ProcessingStatus implements StatusProcessor<TaskDto> {
    @Override
    public ResponseEntity<TaskDto> handle(String taskId) {
        return new ResponseEntity<>(TaskDto.builder()
                .taskId(taskId)
                .status(Status.PROCESSING)
                .build(), HttpStatus.ACCEPTED);
    }
}
