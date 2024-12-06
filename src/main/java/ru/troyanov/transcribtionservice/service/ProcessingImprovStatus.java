package ru.troyanov.transcribtionservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.model.TaskImprovement;

@Component
public class ProcessingImprovStatus implements StatusProcessor<TaskImprovement> {
    @Override
    public ResponseEntity<TaskImprovement> handle(String taskId) {
        return new ResponseEntity<>(TaskImprovement.builder()
                .taskId(taskId)
                .status(Status.PROCESSING)
                .build(), HttpStatus.ACCEPTED);
    }
}
