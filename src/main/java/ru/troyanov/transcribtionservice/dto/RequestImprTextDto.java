package ru.troyanov.transcribtionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestImprTextDto {
    private String lang;
    private String text;
}
