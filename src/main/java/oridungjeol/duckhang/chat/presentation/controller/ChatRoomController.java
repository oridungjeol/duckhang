package oridungjeol.duckhang.chat.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.application.service.ChatService;

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
     * @param room_id
     * @param pageRequest
     * @return
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
     * 유저가 호스트인 채팅방 반환
     * @param uuid
     * @return
     */
    @GetMapping("/chatroom")
    public Long findChatRoomByUuid(
            @AuthenticationPrincipal CustomPrincipal uuid
            ) {
        return chatService.findChatRoomsByUuid(uuid.getName());
    }

    //TODO 유저가 참가자인 채팅방 반환

    //TODO 채팅방 만들기
//    @PostMapping("/chatroom")
//    public void createChatRoom(
//            @AuthenticationPrincipal CustomPrincipal uuid
//    ) {
//        chatService.createChatRoom(uuid.getName());
//    }

    //TODO 채팅방 참가하기
}
