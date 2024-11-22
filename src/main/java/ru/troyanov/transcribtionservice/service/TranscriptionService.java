package ru.troyanov.transcribtionservice.service;

import org.springframework.web.multipart.MultipartFile;
import ru.troyanov.transcribtionservice.dto.TaskDto;

public interface TranscriptionService {
    void doTranscribe(MultipartFile file, String taskId);
}
