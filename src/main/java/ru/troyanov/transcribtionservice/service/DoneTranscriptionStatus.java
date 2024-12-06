package ru.troyanov.transcribtionservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.model.TaskTranscription;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

@Component
@RequiredArgsConstructor
public class DoneTranscriptionStatus implements StatusProcessor<TaskTranscription> {

    private final RedisRepository redisRepository;

    @Override
    public ResponseEntity<TaskTranscription> handle(String taskId) {
        String result = redisRepository.getTaskResult(taskId);
        return new ResponseEntity<>(TaskTranscription.builder()
                .taskId(taskId)
                .status(Status.DONE)
                .taskResult(result)
                .build(), HttpStatus.OK);
    }
}
