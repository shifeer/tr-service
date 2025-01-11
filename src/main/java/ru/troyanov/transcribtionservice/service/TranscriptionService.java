package ru.troyanov.transcribtionservice.service;

import ru.troyanov.transcribtionservice.dto.Language;

import java.io.File;
import java.io.IOException;

public interface TranscriptionService {
    void transcribeFile(File file, String taskId, Language language);
}
