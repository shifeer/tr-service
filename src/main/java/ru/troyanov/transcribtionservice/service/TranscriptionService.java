package ru.troyanov.transcribtionservice.service;

import ru.troyanov.transcribtionservice.dto.Language;

import java.io.File;

public interface TranscriptionService {
    void doTranscribe(File file, String taskId, Language language);
}
