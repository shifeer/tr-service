package ru.troyanov.transcribtionservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.troyanov.transcribtionservice.model.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskTranscriptionDto {
    private String taskId;
    private Status status;
    private String taskResult;
}
