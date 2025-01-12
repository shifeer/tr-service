package ru.troyanov.transcribtionservice.workers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedundantDataDeleter {

    private static final Map<LocalDateTime, Path> DELETE_DATA_PATHS = new HashMap<>();

    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.MINUTES)
    public void deleteRedundantData() throws IOException {
        if (!DELETE_DATA_PATHS.isEmpty()) {
            Set<Map.Entry<LocalDateTime, Path>> entries = DELETE_DATA_PATHS.entrySet();
            Iterator<Map.Entry<LocalDateTime, Path>> iterator = entries.iterator();

            while (iterator.hasNext()) {
                Map.Entry<LocalDateTime, Path> entry = iterator.next();
                if (entry.getKey().isBefore(LocalDateTime.now())) {
                    Files.delete(entry.getValue());
                    iterator.remove();
                }
            }
        }
    }

    public static void putPath(Path path){
        DELETE_DATA_PATHS.put(LocalDateTime.now().plusMinutes(1), path);
    }
}
