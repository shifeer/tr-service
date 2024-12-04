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
import ru.troyanov.transcribtionservice.dto.RequestImprTextDto;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ImprovementTextService {

    private final RedisRepository redisRepository;
    private final ObjectMapper objectMapper;

    @Async
    @SneakyThrows
    public void improvementText(RequestImprTextDto imprTextDto, String taskId) {
        JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode(imprTextDto.getLang()));
        //langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
        List<RuleMatch> matches = langTool.check(imprTextDto.getText());
        Map<String, Map<String, List<String>>> data = getResponseImprTextDto(matches);

        String res = objectMapper.writeValueAsString(data);

        redisRepository.setResult(taskId, res);
    }

    private static @NotNull Map<String, Map<String, List<String>>> getResponseImprTextDto(List<RuleMatch> matches) {
        Map<String, Map<String, List<String>>> results = new LinkedHashMap<>();

        for (RuleMatch match : matches) {
            String key = match.getFromPos() + "-" + match.getToPos();
            Map<String, List<String>> rules = Collections.singletonMap(match.getMessage(), match.getSuggestedReplacements());

            results.put(key, rules);
        }

        return results;
    }
}
