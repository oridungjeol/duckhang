package oridungjeol.duckhang.chat.presentation.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import oridungjeol.duckhang.chat.application.service.ChatService;
import oridungjeol.duckhang.chat.application.service.ChatTestService;

import java.io.IOException;
import java.util.List;

@RequestMapping("/chat")
@RestController
public class ChatRoomTestController {
    private final ChatTestService chatTestService;

    public ChatRoomTestController(ChatService chatService, ChatTestService chatTestService) {
        this.chatTestService = chatTestService;
    }

    @GetMapping("/test/fraud")
    public List<String> checkFraud() throws IOException {
        return chatTestService.checkFraud();
    }
}
