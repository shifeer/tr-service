package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.exception.TaskNotFoundException;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.model.TaskTranscription;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Component
public class StatusTranscriptionHandlerService implements StatusHandler<TaskTranscription> {

    private final Map<Status, StatusProcessor<TaskTranscription>> statusProcessors;
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

    @Override
    public ResponseEntity<TaskTranscription> getResponse(String taskId) {
        Optional<String> statusString = Optional.ofNullable(redisRepository.getTaskStatus(taskId));

        if (statusString.isEmpty()) {
            throw new TaskNotFoundException(taskId);
        }

        Status status = Status.fromString(statusString.get());
        StatusProcessor<TaskTranscription> statusProcessor = statusProcessors.get(status);

        return statusProcessor.handle(taskId);
    }

    @Override
    public ResponseEntity<TaskTranscription> getResponse(Status status, String taskId) {
        StatusProcessor<TaskTranscription> statusProcessor = statusProcessors.get(status);
        return statusProcessor.handle(taskId);
    }
}
