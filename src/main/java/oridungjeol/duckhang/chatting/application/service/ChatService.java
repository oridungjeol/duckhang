package oridungjeol.duckhang.chatting.application.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.chatting.application.dto.Chat;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public String sendMessage(Chat message) {
        String destination = "/topic/chat/" + message.getRoom_id();
        log.info("{}", message.getRoom_id());
        simpMessagingTemplate.convertAndSend(destination, message);
        return destination;
    }
}
