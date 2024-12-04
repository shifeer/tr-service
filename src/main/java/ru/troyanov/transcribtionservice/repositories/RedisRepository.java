package ru.troyanov.transcribtionservice.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import ru.troyanov.transcribtionservice.model.Status;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisRepository {
    protected final RedisTemplate<String, Object> redisTemplate;

    public void createNewTask(String taskId) {
        redisTemplate.opsForHash().put(taskId, "status", Status.PROCESSING.toString());
        redisTemplate.opsForHash().put(taskId, "result", "");
        redisTemplate.expire(taskId, Duration.ofHours(3));
    }

    public String getTaskStatus(String taskId) {
        return (String) redisTemplate.opsForHash().get(taskId, "status");
    }

    public String getTaskResult(String taskId) {
        return (String) redisTemplate.opsForHash().get(taskId, "result");
    }

    public void setResult(String taskId, String result) {
        redisTemplate.opsForHash().put(taskId, "status", Status.DONE.toString());
        redisTemplate.opsForHash().put(taskId, "result", result);
    }

    public void setStatusError(String taskId, Status status) {
        redisTemplate.opsForHash().put(taskId, "status", status.toString());
    }
}
