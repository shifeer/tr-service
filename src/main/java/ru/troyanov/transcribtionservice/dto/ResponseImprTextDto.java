package ru.troyanov.transcribtionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseImprTextDto {
    private Map<String, List<String>> potential_errors;
}
