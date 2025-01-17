FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -DskipTests
COPY src ./src
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y --no-install-recommends ffmpeg && apt-get clean && rm -rf /var/lib/apt/lists/*
COPY --from=build /app/target/*.jar /app/transcripton-service.jar
COPY temp /app/temp
COPY modelsVosk /app/modelsVosk
ENV PATH_TEMP_DIR=/app/temp
ENV PATH_MODELS_DIR=/app/modelsVosk
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/transcripton-service.jar"]