package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskDto;
import ru.troyanov.transcribtionservice.exception.TaskNotFoundException;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Component
public class StatusTranscriptionHandler implements StatusHandler<TaskDto> {

    private final Map<Status, StatusProcessor<TaskDto>> statusProcessors;
    private final RedisRepository redisRepository;

    public StatusTranscriptionHandler(DoneStatus doneStatus,
                                      ErrorStatus errorStatus,
                                      ProcessingStatus processingStatus,
                                      RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
        statusProcessors = new EnumMap<>(Status.class);
        statusProcessors.put(Status.DONE, doneStatus);
        statusProcessors.put(Status.ERROR, errorStatus);
        statusProcessors.put(Status.PROCESSING, processingStatus);
    }

    public ResponseEntity<TaskDto> getResponse(String taskId) {
        Optional<String> statusString = Optional.ofNullable(redisRepository.getTaskStatus(taskId));

        if (statusString.isEmpty()) {
            throw new TaskNotFoundException(taskId);
        }

        Status status = Status.fromString(statusString.get());
        StatusProcessor<TaskDto> statusProcessor = statusProcessors.get(status);

        return statusProcessor.handle(taskId);
    }

    @Override
    public ResponseEntity<TaskDto> getResponse(Status status, String taskId) {
        StatusProcessor<TaskDto> statusProcessor = statusProcessors.get(status);
        return statusProcessor.handle(taskId);
    }
}
