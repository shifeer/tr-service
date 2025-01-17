package ru.troyanov.transcribtionservice.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;
import ru.troyanov.transcribtionservice.dto.Language;

import java.nio.file.Path;
import java.util.Map;

@ConfigurationProperties(prefix = "vosk")
public class LanguageVoskModels {
    private final Map<Language, Path> pathLanguageModels;
    @ConstructorBinding
    public LanguageVoskModels(Map<Language, Path> pathLanguageModels) {
        this.pathLanguageModels = pathLanguageModels;
        this.pathLanguageModels.forEach((key, value) -> this.pathLanguageModels.put(key, Path.of(System.getProperty("user.dir") + value)));
    }

    public Path getPathLanguageModels(Language language) {
        return pathLanguageModels.get(language);
    }
}
