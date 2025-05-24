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
import oridungjeol.duckhang.chat.application.dto.ChatRoom;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository.ChatESRepository;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomEntity;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomParticipantEntity;
import oridungjeol.duckhang.chat.infrastructure.mapper.ChatMapper;
import oridungjeol.duckhang.chat.infrastructure.mapper.ChatRoomMapper;
import oridungjeol.duckhang.chat.infrastructure.repository.ChatRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatESRepository chatESRepository;
    private final ChatMapper chatMapper;
    private final ChatRoomMapper chatRoomMapper;
    private final ChatRepository chatRepository;

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public ChatService(SimpMessagingTemplate simpMessagingTemplate, ChatESRepository chatESRepository, ChatMapper chatMapper, JwtParser jwtParser, ChatRoomMapper chatRoomMapper, ChatRepository chatRepository) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatESRepository = chatESRepository;
        this.chatMapper = chatMapper;
        this.chatRoomMapper = chatRoomMapper;
        this.chatRepository = chatRepository;
    }

    /**
     * 메시지 저장
     * @param message
     * @throws Exception
     */
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
     * @param room_id
     * @param pageable
     * @return
     * @throws JsonProcessingException
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
     * 유저가 참여중인 채팅방 정보를 반환
     * @param uuid
     * @return
     */
    public List<ChatRoom> findChatRoomByUuid(String uuid) {
        List<ChatRoomEntity> chatRoomEntities = chatRepository.findChatRoomByUuid(uuid);
        List<ChatRoomParticipantEntity> chatRoomParticipantEntities = chatRepository.findChatRoomParticipantByUuid(uuid);

        List<ChatRoom> chatRoomList = new ArrayList<>();
        for (ChatRoomEntity chatRoomEntity : chatRoomEntities) {
            chatRoomList.add(chatRoomMapper.chatRoomToDto(chatRoomEntity));
        }

        List<ChatRoom> chatRoomParticipantList = new ArrayList<>();
        for (ChatRoomParticipantEntity chatRoomParticipantEntity : chatRoomParticipantEntities) {
            chatRoomParticipantList.add(chatRoomMapper.chatRoomParticipantToDto(chatRoomParticipantEntity));
        }

        chatRoomList.addAll(chatRoomParticipantList);

        return chatRoomList;
    }
}
