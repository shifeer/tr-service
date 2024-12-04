package ru.troyanov.transcribtionservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskImprovementDto;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class DoneImprovStatus implements StatusProcessor<TaskImprovementDto> {

    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public ResponseEntity<TaskImprovementDto> handle(String taskId) {
        String taskResult = redisRepository.getTaskResult(taskId);
        Map<String, Map<String, List<String>>> errors = objectMapper.readValue(taskResult, Map.class);
        TaskImprovementDto response = TaskImprovementDto.builder()
                .taskId(taskId)
                .status(Status.DONE)
                .potentialErrors(errors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
