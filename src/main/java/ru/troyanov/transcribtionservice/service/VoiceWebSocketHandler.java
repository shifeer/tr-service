package ru.troyanov.transcribtionservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.BinaryWebSocketHandler;
import org.vosk.Model;
import org.vosk.Recognizer;

import java.nio.ByteBuffer;

@Slf4j
@RequiredArgsConstructor
public class VoiceWebSocketHandler extends BinaryWebSocketHandler {

    private final String PATH_MODEL;
    private Model model;
    private Recognizer recognizer;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        model = new Model(PATH_MODEL);
        recognizer = new Recognizer(model, 16000);
        log.info("Voice websocket connection established");
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.info("Voice websocket message received");
        processAudioData(session, message);
    }

    void processAudioData(WebSocketSession session, BinaryMessage message) throws Exception {
        ByteBuffer buffer = message.getPayload();
        recognizer.acceptWaveForm(buffer.array(), buffer.limit());
        String result = extractTextFromJson(recognizer.getResult());
        session.sendMessage(new TextMessage(result));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        recognizer.close();
        model.close();
        recognizer = null;
        model = null;
        log.info("Voice websocket connection closed");
    }

    private String extractTextFromJson(String jsonString) {
        JSONObject jsonObject = new JSONObject(jsonString);
        return jsonObject.optString("text");
    }
}
