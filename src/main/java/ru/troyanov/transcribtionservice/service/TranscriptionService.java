package ru.troyanov.transcribtionservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface TranscriptionService {
    void doTranscribe(MultipartFile file, String taskId);
}
