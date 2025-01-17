package ru.troyanov.transcribtionservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
public class TXTFileService implements FileService {

    @Value("#{systemProperties['user.dir'] + '${path.to.dir}'}")
    private Path pathToDir;

    @Override
    public File generateFile(String content) {
        Path path = pathToDir.resolve(UUID.randomUUID() + ".txt");

        try {
            Files.writeString(path, content, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error with work file");
        }

        return path.toFile();
    }
}
