package ru.troyanov.transcribtionservice.service;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

import static java.nio.file.Files.createTempFile;
import static java.nio.file.Files.write;

@Service
public class MultiToSimpleFileService {

    @Value("${path.to.dir}")
    private String pathToDir;

    @SneakyThrows
    public File multiToFile(MultipartFile multipartFile) {

        byte[] audioBytes = multipartFile.getBytes();
        Path file = createTempFile(Path.of(System.getProperty("user.dir") + pathToDir), multipartFile.getOriginalFilename(), "");
        write(file, audioBytes);

        return file.toFile();
    }
}
