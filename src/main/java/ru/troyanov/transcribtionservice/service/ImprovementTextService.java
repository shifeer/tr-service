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
import ru.troyanov.transcribtionservice.dto.RequestTaskImprovementDto;
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
    public void improvementText(RequestTaskImprovementDto taskImprovementDto, String taskId) {
        JLanguageTool langTool = new JLanguageTool(Languages.getLanguageForShortCode(taskImprovementDto.getLang()));
        //langTool.activateLanguageModelRules(new File("/data/google-ngram-data"));
        List<RuleMatch> matches = langTool.check(taskImprovementDto.getText());
        Map<String, Map<String, List<String>>> data = getTaskImprovementDto(matches);

        String res = objectMapper.writeValueAsString(data);

        redisRepository.setResult(taskId, res);
    }

    private static @NotNull Map<String, Map<String, List<String>>> getTaskImprovementDto(List<RuleMatch> matches) {
        Map<String, Map<String, List<String>>> results = new LinkedHashMap<>();

        for (RuleMatch match : matches) {
            String key = match.getFromPos() + "-" + match.getToPos();
            Map<String, List<String>> rules = Collections.singletonMap(match.getMessage(), match.getSuggestedReplacements());

            results.put(key, rules);
        }

        return results;
    }
}
