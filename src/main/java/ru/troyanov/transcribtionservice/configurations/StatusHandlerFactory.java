package ru.troyanov.transcribtionservice.configurations;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.dto.TaskDto;
import ru.troyanov.transcribtionservice.exception.TaskNotFoundException;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.service.*;
import java.util.EnumMap;
import java.util.Map;

@Component
public class StatusHandlerFactory {

    private final Map<Status, StatusHandler> statusHandlers;

    public StatusHandlerFactory(DoneStatus doneStatus,
                                ErrorStatus errorStatus,
                                ErrorFormatStatus errorFormatStatus,
                                ProcessingStatus processingStatus) {
        statusHandlers = new EnumMap<>(Status.class);
        statusHandlers.put(Status.DONE, doneStatus);
        statusHandlers.put(Status.ERROR, errorStatus);
        statusHandlers.put(Status.ERROR_FORMAT, errorFormatStatus);
        statusHandlers.put(Status.PROCESSING, processingStatus);
    }

    public ResponseEntity<TaskDto> getResponse(Status status, String taskId) {
        if (status == null) {
            throw new TaskNotFoundException(String.format("Task for %s id do not exist", taskId));
        }
        StatusHandler statusHandler = statusHandlers.get(status);
        return statusHandler.handle(taskId);
    }
}
