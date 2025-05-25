package oridungjeol.duckhang.chat.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.auth.infrastructure.jwt.JwtParser;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.application.dto.ChatParam;
import oridungjeol.duckhang.chat.application.dto.ChatRoom;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository.ChatESRepository;

import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomEntity;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomParticipantEntity;
import oridungjeol.duckhang.chat.infrastructure.mapper.ChatMapper;
import oridungjeol.duckhang.chat.infrastructure.mapper.ChatRoomMapper;
import oridungjeol.duckhang.chat.infrastructure.repository.ChatRepository;

import java.util.ArrayList;
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
     * @param message 1개의 채팅 메시지 데이터
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
     * 최신 50개의 메시지를 리턴합니다
     * @param room_id 채팅방 고유 번호
     * @param pageable pageable객체
     * @return 최신 50개의 채팅 데이터
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
     * @param uuid 유저 고유 번호
     * @return 채팅방 리스트
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

    /**
     * 새로운 채팅방을 생성하고 생성된 채팅방 객체를 리턴합니다.
     * @param uuid 유저 고유 번호
     * @param chatParam 채팅방 이름, 게시글 고유 번호, 게시글 타입
     * @return 채팅방 정보
     */
    public ChatRoom createChatRoom(String uuid, ChatParam chatParam) {
        ChatRoomEntity chatRoomEntity = ChatRoomEntity.builder()
                .uuid(uuid)
                .name(chatParam.getName())
                .board_id(chatParam.getBoard_id())
                .type(chatParam.getType())
                .build();

        ChatRoomEntity response = chatRepository.save(chatRoomEntity);
        ChatRoom chatRoomInfo = chatRoomMapper.chatRoomToDto(response);
        return chatRoomInfo;
    }
}
