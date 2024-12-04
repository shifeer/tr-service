package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskTranscriptionDto;
import ru.troyanov.transcribtionservice.exception.TaskNotFoundException;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Component
public class StatusTranscriptionHandlerService implements StatusHandler<TaskTranscriptionDto> {

    private final Map<Status, StatusProcessor<TaskTranscriptionDto>> statusProcessors;
    private final RedisRepository redisRepository;

    public StatusTranscriptionHandlerService(DoneTranscriptionStatus doneTranscriptionStatus,
                                             ErrorTranscriptionStatus errorTranscriptionStatus,
                                             ProcessingTranscriptionStatus processingTranscriptionStatus,
                                             RedisRepository redisRepository) {
        this.redisRepository = redisRepository;
        statusProcessors = new EnumMap<>(Status.class);
        statusProcessors.put(Status.DONE, doneTranscriptionStatus);
        statusProcessors.put(Status.ERROR, errorTranscriptionStatus);
        statusProcessors.put(Status.PROCESSING, processingTranscriptionStatus);
    }

    public ResponseEntity<TaskTranscriptionDto> getResponse(String taskId) {
        Optional<String> statusString = Optional.ofNullable(redisRepository.getTaskStatus(taskId));

        if (statusString.isEmpty()) {
            throw new TaskNotFoundException(taskId);
        }

        Status status = Status.fromString(statusString.get());
        StatusProcessor<TaskTranscriptionDto> statusProcessor = statusProcessors.get(status);

        return statusProcessor.handle(taskId);
    }

    @Override
    public ResponseEntity<TaskTranscriptionDto> getResponse(Status status, String taskId) {
        StatusProcessor<TaskTranscriptionDto> statusProcessor = statusProcessors.get(status);
        return statusProcessor.handle(taskId);
    }
}
