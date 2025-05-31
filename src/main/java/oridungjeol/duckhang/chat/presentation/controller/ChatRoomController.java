package oridungjeol.duckhang.chat.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.application.dto.ChatParam;
import oridungjeol.duckhang.chat.application.dto.ChatRoom;
import oridungjeol.duckhang.chat.application.dto.ChatRoomParticipant;
import oridungjeol.duckhang.chat.application.service.ChatService;

import java.io.IOException;
import java.util.List;

@RequestMapping("/chat")
@RestController
public class ChatRoomController {

    private final ChatService chatService;

    public ChatRoomController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 최근 대화 50개 불러오기
     * @param room_id 채팅방 고유번호
     * @param pageRequest pageable 객체
     * @return 최근 50개 채팅 데이터 pageable 객체
     * @throws JsonProcessingException
     */
    @GetMapping("/recent/{room_id}")
    public List<Chat> findChatByRoom_id(
            @PathVariable("room_id") long room_id,
            Pageable pageRequest
    ) throws JsonProcessingException {
        return chatService.findChatByRoom_id(room_id, pageRequest);
    }

    /**
     * 유저가 호스트인 채팅방, 참가자인 채팅방 반환
     * @param uuid
     * @return 채팅방 리스트
     */
    @GetMapping("/chatroom")
    public List<ChatRoom> findChatRoomByUuid(
            @AuthenticationPrincipal CustomPrincipal uuid
            ) {
        System.out.println("uuid is : " + uuid.getName());

        List<ChatRoom> chatRoomList = chatService.findChatRoomByUuid(uuid.getName());
        return chatRoomList;
    }

    /**
     * 새로운 채팅방을 생성합니다.
     * @param uuid 유저 고유 번호
     * @param chatParam 채팅방 생성 시 필요한 데이터(채팅방 이름, 게시글 고유 번호, 게시글 타입)
     * @return 채팅방 객체
     */
    @PostMapping("/create")
    public ChatRoom createChatRoom(
            @AuthenticationPrincipal CustomPrincipal uuid,
            @RequestBody ChatParam chatParam
    ) {
        ChatRoom existChatRoom = chatService.joinChatRoom(uuid.getName(), chatParam);
        if (existChatRoom == null) {
            return chatService.createChatRoom(uuid.getName(), chatParam);
        } else {
            return existChatRoom;
        }
    }

    /**
     * 이미지를 firebase에 업로드 후 url을 리턴합니다.
     * @param image
     * @return image url
     */
    @PostMapping("/upload/image")
    public String uploadImage(@RequestParam("image") MultipartFile image) {
        return chatService.uploadImage(image);
    }

    @GetMapping("/fraud/{room_id}")
    public List<String> checkFraud(@PathVariable("room_id") long room_id) throws IOException {
        return chatService.checkFraud(room_id);
    }
}
