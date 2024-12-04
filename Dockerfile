FROM openjdk:17-jdk-slim
WORKDIR /app
WORKDIR /app/tempAudiofiles
COPY modelsVosk /app/modelVosk
COPY target/transcription-service-0.0.1-SNAPSHOT.jar /app/transcripton-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/transcripton-service.jar"]