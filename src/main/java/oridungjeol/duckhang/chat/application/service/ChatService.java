package oridungjeol.duckhang.chat.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import oridungjeol.duckhang.auth.infrastructure.jwt.JwtParser;
import oridungjeol.duckhang.chat.application.domain.MessageType;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.application.dto.ChatParam;
import oridungjeol.duckhang.chat.application.dto.ChatRoom;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository.ChatESRepository;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomEntity;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomParticipantEntity;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomParticipantPK;
import oridungjeol.duckhang.chat.infrastructure.mapper.ChatMapper;
import oridungjeol.duckhang.chat.infrastructure.mapper.ChatRoomMapper;
import oridungjeol.duckhang.chat.infrastructure.repository.ChatParticipantRepository;
import oridungjeol.duckhang.chat.infrastructure.repository.ChatRepository;
import oridungjeol.duckhang.common.firebase.storage.FirebaseStorageService;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatESRepository chatESRepository;
    private final ChatMapper chatMapper;
    private final ChatRoomMapper chatRoomMapper;
    private final ChatRepository chatRepository;

    private final FirebaseStorageService firebaseStorageService;

    private Logger log = LoggerFactory.getLogger(this.getClass().getName());

    public ChatService(SimpMessagingTemplate simpMessagingTemplate, ChatESRepository chatESRepository, ChatMapper chatMapper, JwtParser jwtParser, ChatRoomMapper chatRoomMapper, ChatRepository chatRepository, FirebaseStorageService firebaseStorageService) {
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.chatESRepository = chatESRepository;
        this.chatMapper = chatMapper;
        this.chatRoomMapper = chatRoomMapper;
        this.chatRepository = chatRepository;
        this.firebaseStorageService = firebaseStorageService;
    }

    /**
     * 메시지 저장
     *
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

    public String uploadImage(MultipartFile image) {
        String image_url = firebaseStorageService.upload(image);
        log.info(image_url);
        return image_url;
    }

    /**
     * 최신 50개의 메시지를 리턴합니다
     *
     * @param room_id  채팅방 고유 번호
     * @param pageable pageable객체
     * @return 최신 50개의 채팅 데이터
     * @throws JsonProcessingException
     */
    public List<Chat> findChatByRoom_id(long room_id, Pageable pageable) throws JsonProcessingException {
        Page<ChatDocument> chatDocumentList = chatESRepository.findChatByRoomId(room_id, pageable);

        List<Chat> chatList = new ArrayList<>();
        for (ChatDocument chatDocument : chatDocumentList) {
            chatList.add(chatMapper.chatDocumentToDto(chatDocument));
        }

        return chatList;
    }

    /**
     * 유저가 참여중인 채팅방 정보를 반환
     *
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
     * 채팅방이 존재한다면 해당 채팅방을 리턴합니다.
     *
     * @param uuid      채팅방을 생성하려는 user uuid
     * @param chatParam 채팅방 정보
     * @return ChatRoom
     */
    public ChatRoom joinChatRoom(String uuid, ChatParam chatParam) {
        ChatRoomParticipantEntity chatRoomParticipantEntity = chatParticipantRepository.findChatRoomIsExist(uuid, chatParam.getBoard_id());

        if (chatRoomParticipantEntity == null) {
            return null;
        } else {
            return chatRoomMapper.chatRoomParticipantToDto(chatRoomParticipantEntity);
        }
    }

    /**
     * 새로운 채팅방을 생성하고 생성된 채팅방 객체를 리턴합니다.
     *
     * @param uuid      유저 고유 번호
     * @param chatParam 채팅방 이름, 게시글 고유 번호, 게시글 타입
     * @return 채팅방 정보
     */
    public ChatRoom createChatRoom(String uuid, ChatParam chatParam) {
        //없다면 새로운 채팅방 생성
        ChatRoomEntity newChatRoom = ChatRoomEntity.builder()
                .uuid(chatParam.getAuthor_uuid())
                .name(chatParam.getName())
                .board_id(chatParam.getBoard_id())
                .type(chatParam.getType())
                .build();

        ChatRoomEntity response = chatRepository.save(newChatRoom);

        //자기 자신은 참가자로 채팅방 참여
        ChatRoomParticipantEntity newChatRoomParticipant = ChatRoomParticipantEntity.builder()
                .participant_id(ChatRoomParticipantPK.builder()
                        .room_id(response.getRoom_id())
                        .uuid(uuid)
                        .build())
                .name(response.getName())
                .board_id(response.getBoard_id())
                .type(response.getType())
                .build();

        ChatRoomParticipantEntity participantResponse = chatParticipantRepository.save(newChatRoomParticipant);

        //글쓴이의 user name 가져오기
        User user = userJpaRepository.findByUuid(UUID.fromString(chatParam.getAuthor_uuid()))
                .orElseThrow(() -> new IllegalArgumentException("해당 uuid에 대한 사용자를 찾을 수 없습니다."));

        //시스템 메시지 저장
        ChatDocument chatDocument = ChatDocument.builder()
                .type(MessageType.SYSTEM)
                .authorUuid(uuid)
                .content(chatParam.getName() + " 채팅이 시작되었어요.")
                .createdAt(LocalDateTime.now())
                .roomId(response.getRoom_id())
                .build();

        chatESRepository.save(chatDocument);

        ChatRoom chatRoomInfo = chatRoomMapper.chatRoomToDto(response);
        return chatRoomInfo;
    }
}
