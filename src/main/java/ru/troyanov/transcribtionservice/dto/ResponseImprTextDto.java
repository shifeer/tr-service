package ru.troyanov.transcribtionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.troyanov.transcribtionservice.model.Status;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseImprTextDto {
    private String taskId;
    private Status status;
    private Map<String, Map<String, List<String>>> potentialErrors;
}
