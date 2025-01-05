package ru.troyanov.transcribtionservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.languagetool.JLanguageTool;
import org.languagetool.Languages;
import org.languagetool.rules.RuleMatch;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.troyanov.transcribtionservice.dto.Language;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImprovementTextService {

    @SneakyThrows
    public String improvementText(String text, Language language) {
        JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode(language.getValue()));
        //langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
        StringBuilder correctText = new StringBuilder(text);
        List<RuleMatch> matches = langTool.check(text);
        int offset = 0;

        for (RuleMatch match : matches) {
            List<String> suggestedReplacements = match.getSuggestedReplacements();
            if (!suggestedReplacements.isEmpty()) {
                String replacement = suggestedReplacements.get(0);
                correctText.replace(match.getFromPos() + offset, match.getToPos() + offset, replacement);
                offset += replacement.length() - (match.getToPos() - match.getFromPos());
            } else {
                System.out.println(match.getMessage());
            }
        }
        return correctText.toString();
    }
}
