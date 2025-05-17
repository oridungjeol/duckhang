package oridungjeol.duckhang.chatting.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import oridungjeol.duckhang.chatting.application.dto.Chat;
import oridungjeol.duckhang.chatting.application.service.ChatService;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("chat/{room_id}")
    public String sendMessage(@DestinationVariable("room_id") long room_id, Chat message) {
        return chatService.sendMessage(message);
    }
}
