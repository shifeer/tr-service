package ru.troyanov.transcribtionservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskDto;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

@Component
@RequiredArgsConstructor
public class DoneStatus implements StatusHandler {

    private final RedisRepository redisRepository;

    @Override
    public ResponseEntity<TaskDto> handle(String taskId) {
        String result = redisRepository.getTaskResult(taskId);
        return new ResponseEntity<>(TaskDto.builder()
                .taskId(taskId)
                .status(Status.DONE)
                .taskResult(result)
                .build(), HttpStatus.OK);
    }
}
