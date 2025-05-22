package oridungjeol.duckhang.chat.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.infrastructure.ChatRepository;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository.ChatESRepository;
import oridungjeol.duckhang.chat.infrastructure.redis.RedisChat;
import oridungjeol.duckhang.chat.infrastructure.redis.consumer.RedisStreamsChatConsumer;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;
import oridungjeol.duckhang.chat.infrastructure.redis.producer.RedisStreamsChatProducer;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;
    @Qualifier("redisTemplate")
    private final RedisTemplate<String, String> redisTemplate;
    private final ChatESRepository chatESRepository;

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public ChatService(SimpMessagingTemplate simpMessagingTemplate, ChatRepository chatRepository, RedisTemplate<String, String> redisTemplate, RedisStreamsChatConsumer redisStreamsChatConsumer, RedisStreamsChatProducer redisStreamsChatProducer, RedisChat redisChat, ChatESRepository chatESRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatRepository = chatRepository;
        this.redisTemplate = redisTemplate;
        this.chatESRepository = chatESRepository;
    }

    public void sendMessage(Chat message) throws Exception {
        ChatEntity chatEntity = message.toEntity();;
        try {
            chatRepository.save(chatEntity);
        } catch (Exception e) {
            log.error("메시지 DB에 저장 중 오류 발생");
            throw new Exception(e);
        }

        try {
            ChatDocument chatDocument = ChatDocument.toChatDocument(chatEntity);
            chatESRepository.save(chatDocument);
        } catch (Exception e) {
            log.error("메시지 Elastic Search에 저장 중 오류 발생");
            throw new Exception(e);
        }

        try {
            String destination = "/topic/chat/" + message.getRoom_id();
            simpMessagingTemplate.convertAndSend(destination, message);
        } catch (Exception e) {
            log.error("메시지 broadcast 중 오류 발생");
        }
    }

    /**
     * 최신 50개의 메시지를 리턴
     */
    public List<Chat> findRecentChattingByRoom_id(long room_id) throws JsonProcessingException {
        List<String> recentChatList = redisTemplate.opsForList().range("recent-chat:" + room_id, 0, 49);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        List<Chat> chatList = new ArrayList<>();
        for (String recentChat : recentChatList) {
            ChatEntity chatEntity = objectMapper.readValue(recentChat, ChatEntity.class);
            chatList.add(chatEntity.chatEntityToDto());
        }

        return chatList;
    }
}
