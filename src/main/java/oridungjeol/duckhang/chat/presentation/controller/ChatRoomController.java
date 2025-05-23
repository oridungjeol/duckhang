package oridungjeol.duckhang.chat.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.application.service.ChatService;

import java.util.List;

@RestController
public class ChatRoomController {

    private final ChatService chatService;

    public ChatRoomController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("chat/recent/{room_id}")
    public List<Chat> findChatByRoom_id(
            @PathVariable("room_id") long room_id,
            Pageable pageRequest
    ) throws JsonProcessingException {
        return chatService.findChatByRoom_id(room_id, pageRequest);
    }
}
