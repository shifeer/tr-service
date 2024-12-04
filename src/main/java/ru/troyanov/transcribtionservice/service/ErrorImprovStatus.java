package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.ResponseImprTextDto;
import ru.troyanov.transcribtionservice.exception.TaskExecuteException;

@Component
public class ErrorImprovStatus implements StatusProcessor<ResponseImprTextDto> {
    @Override
    public ResponseEntity<ResponseImprTextDto> handle(String taskId) {
        throw new TaskExecuteException("Error when working with the task");
    }
}
