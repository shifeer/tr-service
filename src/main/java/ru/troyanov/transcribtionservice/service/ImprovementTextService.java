package ru.troyanov.transcribtionservice.service;

import lombok.SneakyThrows;
import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.troyanov.transcribtionservice.dto.RequestImprTextDto;
import ru.troyanov.transcribtionservice.dto.ResponseImprTextDto;

import java.util.*;

@Service
public class ImprovementTextService {

    @SneakyThrows
    public ResponseEntity<ResponseImprTextDto> improvementText(RequestImprTextDto imprTextDto) {
        JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode(imprTextDto.getLang()));
        //langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
        List<RuleMatch> matches = langTool.check(imprTextDto.getText());
        Map<String, List<String>> results = new LinkedHashMap<>();

        for (RuleMatch match : matches) {
            results.put(match.getFromPos() + "-" + match.getToPos() + ": " + match.getMessage(), match.getSuggestedReplacements());
        }

        ResponseImprTextDto responseImprTextDto = new ResponseImprTextDto();
        responseImprTextDto.setPotential_errors(results);

        return new ResponseEntity<>(responseImprTextDto, HttpStatus.OK);
    }
}
