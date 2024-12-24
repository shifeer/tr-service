package ru.troyanov.transcribtionservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.troyanov.transcribtionservice.dto.Format;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileAttribute;
import java.time.LocalDateTime;

import static java.nio.file.Files.createTempFile;

@Service
public class TXTFileService implements FileService {

    @Value("${}")
    private Path pathToDir;

    @Override
    public File generateFile(String content) {
        LocalDateTime now = LocalDateTime.now();
        Path path = pathToDir.resolve(pathToDir + now.toString() + ".txt");
        try {
            Files.writeString(path, content, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error with work file");
        }

        return path.toFile();
    }
}
