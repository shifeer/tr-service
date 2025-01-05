package ru.troyanov.transcribtionservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.Status;
import ru.troyanov.transcribtionservice.dto.TaskTranscriptionDTO;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

@Component
@RequiredArgsConstructor
public class DoneTranscriptionStatus implements StatusProcessor<TaskTranscriptionDTO> {

    private final RedisRepository redisRepository;

    @Override
    public ResponseEntity<TaskTranscriptionDTO> handle(String taskId) {
        String result = redisRepository.getTaskResult(taskId);
        return new ResponseEntity<>(TaskTranscriptionDTO.builder()
                .taskId(taskId)
                .status(Status.DONE)
                .taskResult(result)
                .build(), HttpStatus.OK);
    }
}
