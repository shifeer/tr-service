package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;
import ru.troyanov.transcribtionservice.model.Status;

public interface StatusHandler<D> {
    ResponseEntity<D> getResponse(String taskId);
    ResponseEntity<D> getResponse(Status status, String taskId);
}
