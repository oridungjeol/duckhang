package oridungjeol.duckhang.chat.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.auth.infrastructure.jwt.JwtParser;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository.ChatESRepository;
import oridungjeol.duckhang.chat.infrastructure.mapper.ChatMapper;
import oridungjeol.duckhang.chat.infrastructure.repository.ChatRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatESRepository chatESRepository;
    private final ChatMapper chatMapper;
    private final ChatRepository chatRepository;

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public ChatService(SimpMessagingTemplate simpMessagingTemplate, ChatESRepository chatESRepository, ChatMapper chatMapper, JwtParser jwtParser, ChatRepository chatRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatESRepository = chatESRepository;
        this.chatMapper = chatMapper;
        this.chatRepository = chatRepository;
    }

    public void sendMessage(Chat message) throws Exception {
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
    public List<Chat> findChatByRoom_id(long room_id, Pageable pageable) throws JsonProcessingException {
        Page<ChatDocument> chatDocumentList = chatESRepository.findChatByRoomId(room_id, pageable);

        List<Chat> chatList = new ArrayList<>();
        for (ChatDocument chatDocument: chatDocumentList) {
            chatList.add(chatMapper.chatDocumentToDto(chatDocument));
        }

        return chatList;
    }

    /**
     * 유저가 속한 채팅방 id 리스트를 반환
     * @param uuid
     * @return
     */
    public Long findChatRoomsByUuid(String uuid) {
        Long room_id = chatRepository.findChatRoomByUuid(uuid);
        if (room_id == null) {
            return 0L;
        }
        return room_id;
    }
}
