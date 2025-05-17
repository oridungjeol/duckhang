package oridungjeol.duckhang.chat.application.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.infrastructure.ChatRepository;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;
    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public String sendMessage(Chat message) {
        String destination = "/topic/chat/" + message.getRoom_id();
        simpMessagingTemplate.convertAndSend(destination, message);

        ChatEntity chatEntity = message.toEntity();

        chatRepository.save(chatEntity);

        return destination;
    }
}
