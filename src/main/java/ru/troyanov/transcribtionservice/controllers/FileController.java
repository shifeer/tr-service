package ru.troyanov.transcribtionservice.controllers;

import lombok.RequiredArgsConstructor;
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
import ru.troyanov.transcribtionservice.service.FileService;

import java.io.File;
import java.net.MalformedURLException;

@RequestMapping("/file")
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    public ResponseEntity<Resource> getFile(String content, @RequestParam Format format) {
        File result = fileService.generateFile(content);
        Resource resource = null;

        try {
            resource = new UrlResource(result.toURI());
        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error with work file");
        }

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
}
