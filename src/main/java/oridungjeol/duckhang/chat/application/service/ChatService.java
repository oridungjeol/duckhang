package oridungjeol.duckhang.chat.application.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.infrastructure.ChatRepository;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;

import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;
    private final RedisTemplate<Integer, ChatEntity> redisTemplate;

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    /**
     *
     * 채팅 데이터 insert
     * mysql, redis에 저장
     */
    public String sendMessage(Chat message) {
        String destination = "/topic/chat/" + message.getRoom_id();
        simpMessagingTemplate.convertAndSend(destination, message);

        try {
            ChatEntity chatEntity = message.toEntity();
            chatRepository.save(chatEntity);

            ObjectRecord record = StreamRecords.newRecord()
                    .ofObject(chatEntity)
                    .withStreamKey("chat-room" + chatEntity.getRoom_id());

            RecordId recordId = redisTemplate.opsForStream().add(record);
        } catch (Exception e) {
            log.error("메시지 저장 중 오류 발생");
        }

        return destination;
    }

}
