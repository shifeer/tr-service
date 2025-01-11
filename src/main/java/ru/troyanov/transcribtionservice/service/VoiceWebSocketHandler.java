package ru.troyanov.transcribtionservice.service;

import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.io.ByteArrayInputStream;

@RequiredArgsConstructor
public class VoiceWebSocketHandler extends BinaryWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(VoiceWebSocketHandler.class);
    private final String PATH_MODEL;
    private Model model;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        model = new Model(System.getProperty("user.dir") + PATH_MODEL);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        processAudioData(session, message);
    }

    void processAudioData(WebSocketSession session, BinaryMessage message) throws Exception {
        byte[] audioData = message.getPayload().array();
        StringBuilder sb = new StringBuilder();

        try (Recognizer recognizer = new Recognizer(model, 16000);
             ByteArrayInputStream audioStream = new ByteArrayInputStream(audioData)) {
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = audioStream.read(buffer)) != -1) {
                if (recognizer.acceptWaveForm(buffer, bytesRead)) {
                    sb.append(extractTextFromJson(recognizer.getResult())).append(" ");
                    System.out.println(sb.toString());
                    session.sendMessage(new TextMessage(sb.toString()));
                }
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        model.close();
        model = null;
    }

    private String extractTextFromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.optString("text");
    }
}
