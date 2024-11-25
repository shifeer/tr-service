package ru.troyanov.transcribtionservice.it;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import ru.troyanov.transcribtionservice.configurations.StatusHandlerFactory;
import ru.troyanov.transcribtionservice.dto.TaskDto;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;
import ru.troyanov.transcribtionservice.service.TranscriptionService;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TranscriptionControllerIT {
    @LocalServerPort
    private int port;

    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private TranscriptionService transcriptionService;
    @Autowired
    private StatusHandlerFactory statusHandlerFactory;

    @Test
    public void givenSuccessResponse_whenGetTranscription_thenSuccess() {
        String url = "http://localhost:" + port + "/api/v1/transcription";

        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

        params.add("file", new ClassPathResource("audio_2024-09-30_22-25-01.ogg"));

        ResponseEntity<TaskDto> response = restTemplate
                .exchange(url, HttpMethod.POST, new HttpEntity<>(params), TaskDto.class);



        assertThat(response.getStatusCodeValue()).isEqualTo(202);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getTaskResult()).isEqualTo("Task in processing");
    }

    @Test
    public void givenSuccessResponse_whenPostTranscription_thenSuccess() {
        String url = "http://localhost:" + port + "/api/v1/transcription";
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("file", new ClassPathResource("audio_2024-09-30_22-25-01.ogg"));
        ResponseEntity<TaskDto> response = restTemplate
                .exchange(url, HttpMethod.POST, new HttpEntity<>(params), TaskDto.class);

        String taskId = response.getBody().getTaskId();
        String urlTest = "http://localhost:" + port + "/api/v1/transcription/" + taskId;
        RestTemplate restTemplateTest = new RestTemplate();
        TaskDto res = restTemplateTest.getForObject(urlTest, TaskDto.class);

        assertThat(res.getTaskId()).isEqualTo(taskId);
        assertThat(res.getStatus()).isEqualTo(Status.PROCESSING);

        while (res.getStatus() == Status.PROCESSING) {
            restTemplateTest = new RestTemplate();
            res = restTemplateTest.getForObject(urlTest, TaskDto.class);
        }

        assertThat(res.getStatus()).isEqualTo(Status.DONE);
        assertThat(res.getTaskResult()).isNotNull();
    }
}
