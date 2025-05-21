package oridungjeol.duckhang.chat.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.infrastructure.ChatRepository;
import oridungjeol.duckhang.chat.infrastructure.consumer.RedisChatConsumer;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;
import oridungjeol.duckhang.chat.infrastructure.producer.RedisChatProducer;

@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;
    @Qualifier("redisTemplate")
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisChatProducer redisChatProducer;

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public ChatService(SimpMessagingTemplate simpMessagingTemplate, ChatRepository chatRepository, RedisTemplate<String, String> redisTemplate, RedisChatConsumer redisChatConsumer, RedisChatProducer redisChatProducer) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatRepository = chatRepository;
        this.redisTemplate = redisTemplate;
        this.redisChatProducer = redisChatProducer;
    }

    /**
     * Redis producer 코드
     * mysql에 채팅 데이터 insert
     * db 저장 -> redis 저장 -> websocket broadcast
     */
    public void sendMessage(Chat message) throws Exception {
        ChatEntity chatEntity = message.toEntity();;
        try {
            chatRepository.save(chatEntity);
        } catch (Exception e) {
            log.error("메시지 DB에 저장 중 오류 발생");
            throw new Exception(e);
        }

        try {
            redisChatProducer.saveInRedis(redisTemplate, chatEntity);
        } catch (Exception e) {
            log.error("메시지 Redis에 저장 중 오류 발생");
            throw new Exception(e);
        }

        try {
            String destination = "/topic/chat/" + message.getRoom_id();
            simpMessagingTemplate.convertAndSend(destination, message);
        } catch (Exception e) {
            log.error("메시지 broadcast 중 오류 발생");
        }
    }

}
