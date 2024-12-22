package ru.troyanov.transcribtionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskTranscriptionDTO {
    private String taskId;
    private Status status;
    private String taskResult;
}
