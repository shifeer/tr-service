package ru.troyanov.transcribtionservice.service;

import java.io.File;

public interface TranscriptionService {
    void doTranscribe(File file, String taskId);
}
