package ru.troyanov.transcribtionservice.dto;

import java.util.Optional;

public enum Language {
    RUSSIAN("ru-RU"),
    ENGLISH("en-US"),;

    private String value;

    Language (String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Optional<Language> fromValue(String value) {
        for (Language language : Language.values()) {
            if (language.getValue().equals(value)) {
                return Optional.of(language);
            }
        }
        return Optional.empty();
    }
}
