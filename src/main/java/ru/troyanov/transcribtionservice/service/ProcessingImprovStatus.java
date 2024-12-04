package ru.troyanov.transcribtionservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.ResponseImprTextDto;
import ru.troyanov.transcribtionservice.model.Status;

@Component
public class ProcessingImprovStatus implements StatusProcessor<ResponseImprTextDto> {
    @Override
    public ResponseEntity<ResponseImprTextDto> handle(String taskId) {
        return new ResponseEntity<>(ResponseImprTextDto.builder()
                .taskId(taskId)
                .status(Status.PROCESSING)
                .build(), HttpStatus.ACCEPTED);
    }
}
