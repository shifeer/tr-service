package ru.troyanov.transcribtionservice.service;

import org.springframework.http.ResponseEntity;

public interface StatusProcessor<D> {
    ResponseEntity<D> handle(String taskId);
}
