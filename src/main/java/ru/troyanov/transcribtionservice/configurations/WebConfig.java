package ru.troyanov.transcribtionservice.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Value("@{}")
    private String pathToDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:" + "/mnt/sdb3/Downloads/proj/transcription-service/src/main/resources/static/img/");
        registry.addResourceHandler();
    }
}
