package ru.troyanov.transcribtionservice.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.troyanov.transcribtionservice.exception.ConvertExtensionToWavException;
import ru.troyanov.transcribtionservice.workers.RedundantDataDeleter;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.time.LocalDateTime;

@Service
@Slf4j
public class ConverterExtensionToWavService {

    public File convertToWav(File file) {

        if (isWav16kHzMono(file)) {
            return file;
        }

        return convert(file);
    }

    @SneakyThrows
    private File convert(File audioFile) {
        File resFile = new File(audioFile.getAbsolutePath() + ".wav");
        ProcessBuilder processBuilder = new ProcessBuilder("ffmpeg", "-i", audioFile.getAbsolutePath(), "-ar", "16000", "-ac", "1", resFile.getAbsolutePath());
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        while ((line = bufferedReader.readLine()) != null) {
            log.info(line);
        }

        int exitCode = process.waitFor();

        if (exitCode == 0) {
            RedundantDataDeleter.putPath(audioFile.toPath());
            return resFile;
        } else {
            log.error("{} converted failed", audioFile.getAbsolutePath());
            RedundantDataDeleter.putPath(audioFile.toPath());
            throw new ConvertExtensionToWavException("Failed converted file to \"WAV, MONO, 16KHz\"");
        }
    }

    @SneakyThrows
    private boolean isWav16kHzMono(File audioFile) {

        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat audioFormat = audioInputStream.getFormat();

            if (audioFormat.getSampleRate() == 16000 && audioFormat.getChannels() == 1 && audioFormat.getEncoding() == AudioFormat.Encoding.PCM_SIGNED) {
                return true;
            }

        } catch (UnsupportedAudioFileException e) {
            log.warn("Unsupported audio file: {}", audioFile.getAbsolutePath());
        }
        return false;
    }
}
