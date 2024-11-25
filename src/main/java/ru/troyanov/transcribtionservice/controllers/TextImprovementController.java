package ru.troyanov.transcribtionservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.troyanov.transcribtionservice.dto.RequestImprTextDto;
import ru.troyanov.transcribtionservice.dto.ResponseImprTextDto;
import ru.troyanov.transcribtionservice.service.ImprovementTextService;

@RestController
@RequestMapping("/api/improvement")
@RequiredArgsConstructor
public class TextImprovementController {

    private final ImprovementTextService improvementTextService;

    @GetMapping
    public ResponseEntity<ResponseImprTextDto> getImprovementText(@RequestBody RequestImprTextDto imprTextDto) {
        return improvementTextService.improvementText(imprTextDto);
    }
}
