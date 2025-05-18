package oridungjeol.duckhang.chat.application.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
     * 채팅 데이터 insert
     * mysql, redis에 저장
     */
    public String sendMessage(Chat message) {
        String destination = "/topic/chat/" + message.getRoom_id();
        simpMessagingTemplate.convertAndSend(destination, message);

        try {
            ChatEntity chatEntity = message.toEntity();
            chatRepository.save(chatEntity);
            redisChatProducer.saveInRedis(redisTemplate, chatEntity);
        } catch (Exception e) {
            log.error("메시지 저장 중 오류 발생");
        }

        return destination;
    }

}
