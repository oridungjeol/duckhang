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
import oridungjeol.duckhang.chat.infrastructure.mapper.ChatMapper;
import oridungjeol.duckhang.chat.infrastructure.redis.RedisChat;
//import oridungjeol.duckhang.chat.infrastructure.redis.consumer.RedisStreamsChatConsumer;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;
import oridungjeol.duckhang.chat.infrastructure.redis.producer.RedisStreamsChatProducer;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatRepository chatRepository;
    private final ChatESRepository chatESRepository;
    private final ChatMapper chatMapper;

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public ChatService(SimpMessagingTemplate simpMessagingTemplate, ChatRepository chatRepository, ChatESRepository chatESRepository, ChatMapper chatMapper) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatRepository = chatRepository;
        this.chatESRepository = chatESRepository;
        this.chatMapper = chatMapper;
    }

    public void sendMessage(Chat message) throws Exception {
        ChatEntity chatEntity = chatMapper.chatToEntity(message);;
//        try {
//            chatRepository.save(chatEntity);
//        } catch (Exception e) {
//            log.error("메시지 DB에 저장 중 오류 발생");
//            throw new Exception(e);
//        }

        try {
            ChatDocument chatDocument = chatMapper.toChatDocument(message);
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
    public List<Chat> findChatByRoom_id(long room_id) throws JsonProcessingException {
        List<ChatDocument> chatDocumentList = chatESRepository.findChatByRoomId(room_id);

        List<Chat> chatList = new ArrayList<>();
        for (ChatDocument chatDocument: chatDocumentList) {
            chatList.add(chatMapper.chatDocumentToDto(chatDocument));
            log.info(chatMapper.chatDocumentToDto(chatDocument).getContent());
        }

        return chatList;
    }
}
