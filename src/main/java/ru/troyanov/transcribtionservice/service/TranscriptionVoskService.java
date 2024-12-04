package ru.troyanov.transcribtionservice.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.vosk.Model;
import org.vosk.Recognizer;
import ru.troyanov.transcribtionservice.model.Status;
import ru.troyanov.transcribtionservice.repositories.RedisRepository;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static java.nio.file.Files.delete;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranscriptionVoskService implements TranscriptionService {

    @Value("${vosk.path-model}")
    private String PATH_MODEL;
    private final RedisRepository redisRepository;
    private final ConverterExtensionToWavService converterExtensionService;

    @Override
    @Async
    public void doTranscribe(File file, String taskId) {
        redisRepository.createNewTask(taskId);
        File audioFile = converterExtensionService.convertToWav(file);
        String recognize = recognize(audioFile, taskId);
        if (recognize != null) {
            redisRepository.setResult(taskId, recognize);
        }
    }

    @SneakyThrows(IOException.class)
    private String recognize(File file, String taskId) {
        try (Model model = new Model(PATH_MODEL);
             AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(new FileInputStream(file)));
             Recognizer recognizer = new Recognizer(model, 16000)) {

            StringBuilder sb = new StringBuilder();
            int nbytes;
            byte[] b = new byte[4096];

            while ((nbytes = ais.read(b)) > 0) {
                if (recognizer.acceptWaveForm(b, nbytes)) {
                    sb.append(extractTextFromJson(recognizer.getResult())).append(" ");
                }
            }

            sb.append(extractTextFromJson(recognizer.getFinalResult()));

            log.info("{} is transcribed", file.getAbsolutePath());

            return sb.toString();

        } catch (UnsupportedAudioFileException e) {
            log.warn("Error for {}", file.getName());
            redisRepository.setStatusError(taskId, Status.ERROR);
            return null;
        } finally {
            delete(file.toPath());
        }
    }

    private String extractTextFromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.optString("text");
    }
}

