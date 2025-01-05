package ru.troyanov.transcribtionservice.dto;

import java.util.Optional;

public enum Format {
    TXT("txt"),
    DOCx("docx");

    private String formatString;

    Format(String formatString) {
        this.formatString = formatString;
    }

    public String getFormatString() {
        return formatString;
    }

    public static Optional<Format> getFormat(String formatString) {
        for (Format format : Format.values()) {
            if (format.getFormatString().equals(formatString)) {
                return Optional.of(format);
            }
        }
        return Optional.empty();
    }
}
