package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import ru.troyanov.transcribtionservice.dto.Status;

public interface StatusHandler<T> {
    ResponseEntity<T> getResponse(String taskId);
    ResponseEntity<T> getResponse(Status status, String taskId);
}
