package ru.troyanov.transcribtionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class TranscriptionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TranscriptionServiceApplication.class, args);
    }

}

