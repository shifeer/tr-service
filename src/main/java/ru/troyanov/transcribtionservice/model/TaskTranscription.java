package ru.troyanov.transcribtionservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskTranscription {
    private String taskId;
    private Status status;
    private String taskResult;
}
