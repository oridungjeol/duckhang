package oridungjeol.duckhang.chat.presentation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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
    public List<Chat> findRecentChattingByRoom_id(@PathVariable("room_id") long room_id) throws JsonProcessingException {
        return chatService.findRecentChattingByRoom_id(room_id);
    }
}
