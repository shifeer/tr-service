package ru.troyanov.transcribtionservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.Status;
import ru.troyanov.transcribtionservice.dto.TaskImprovementDTO;

@Component
public class ProcessingImprovStatus implements StatusProcessor<TaskImprovementDTO> {
    @Override
    public ResponseEntity<TaskImprovementDTO> handle(String taskId) {
        return new ResponseEntity<>(TaskImprovementDTO.builder()
                .taskId(taskId)
                .status(Status.PROCESSING)
                .build(), HttpStatus.ACCEPTED);
    }
}
