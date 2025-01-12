package ru.troyanov.transcribtionservice.controllers;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.troyanov.transcribtionservice.dto.Format;
import ru.troyanov.transcribtionservice.service.DOCXFileService;
import ru.troyanov.transcribtionservice.service.FileService;
import ru.troyanov.transcribtionservice.service.PDFileService;
import ru.troyanov.transcribtionservice.service.TXTFileService;
import ru.troyanov.transcribtionservice.workers.RedundantDataDeleter;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

@Tag(name = "FileController")
@RequestMapping("/files")
@RestController
public class FileController {

    private final Map<Format, FileService> fileServices;

    public FileController(TXTFileService txtFileService,
                          DOCXFileService docxFileService,
                          PDFileService pdFileService) {
        this.fileServices = new EnumMap<>(Format.class);
        this.fileServices.put(Format.TXT, txtFileService);
        this.fileServices.put(Format.DOCX, docxFileService);
        this.fileServices.put(Format.PDF, pdFileService);
    }

    @PostMapping
    public ResponseEntity<Resource> getFile(String content, @RequestParam Format format) {
        File result = fileServices.get(format).generateFile(content);

        try {
            Resource resource = new UrlResource(result.toURI());
            return new ResponseEntity<>(resource, HttpStatus.OK);
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error with work file");
        } finally {
            Files.delete(result.toPath());
        }
    }
}
