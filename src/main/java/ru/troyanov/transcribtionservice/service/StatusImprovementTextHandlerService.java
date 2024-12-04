package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.troyanov.transcribtionservice.dto.ResponseImprTextDto;
import ru.troyanov.transcribtionservice.exception.TaskNotFoundException;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

@Service
public class StatusImprovementTextHandlerService implements StatusHandler<ResponseImprTextDto> {

    private final RedisRepository redisRepository;
    private final Map<Status, StatusProcessor<ResponseImprTextDto>> statusProcessorMap;

    public StatusImprovementTextHandlerService(RedisRepository redisRepository,
                                               DoneImprovStatus doneImprovStatus,
                                               ProcessingImprovStatus processingImprovStatus,
                                               ErrorImprovStatus errorImprovStatus) {
        this.redisRepository = redisRepository;
        statusProcessorMap = new EnumMap<>(Status.class);
        statusProcessorMap.put(Status.DONE, doneImprovStatus);
        statusProcessorMap.put(Status.PROCESSING, processingImprovStatus);
        statusProcessorMap.put(Status.ERROR, errorImprovStatus);
    }

    @Override
    public ResponseEntity<ResponseImprTextDto> getResponse(String taskId) {
        Optional<String> statusString = Optional.ofNullable(redisRepository.getTaskStatus(taskId));

        if (statusString.isEmpty()) {
            throw new TaskNotFoundException(taskId);
        }

        return statusProcessorMap.get(Status.fromString(statusString.get()))
                .handle(taskId);
    }

    @Override
    public ResponseEntity<ResponseImprTextDto> getResponse(Status status, String taskId) {
        StatusProcessor<ResponseImprTextDto> statusProcessor = statusProcessorMap.get(status);
        return statusProcessor.handle(taskId);
    }
}
