package oridungjeol.duckhang.chat.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.application.service.ChatService;

@RequiredArgsConstructor
@Controller
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("chat/{room_id}")
    public void sendMessage(@DestinationVariable("room_id") long room_id, Chat message) throws Exception {
        chatService.sendMessage(message);
    }
}
