package ru.troyanov.transcribtionservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskImprovementDto;
import ru.troyanov.transcribtionservice.model.Status;

@Component
public class ProcessingImprovStatus implements StatusProcessor<TaskImprovementDto> {
    @Override
    public ResponseEntity<TaskImprovementDto> handle(String taskId) {
        return new ResponseEntity<>(TaskImprovementDto.builder()
                .taskId(taskId)
                .status(Status.PROCESSING)
                .build(), HttpStatus.ACCEPTED);
    }
}
