package oridungjeol.duckhang.chat.application.service;

import org.springframework.stereotype.Service;

@Service
public class ChatUserService {
    public void changeStatusToConnect(String uuid) {
        // TODO 레디스에 상태 저장

    }

    public void changeStatusToDisconnect(String uuid) {
        // TODO 레디스에 상태 저장
        System.out.println("websocket disconnect in service");
    }
}
