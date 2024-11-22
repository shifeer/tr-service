package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import ru.troyanov.transcribtionservice.dto.TaskDto;

public interface StatusHandler {
    ResponseEntity<TaskDto> handle(String taskId);
}
