package oridungjeol.duckhang.chat.infrastructure.websocket;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import oridungjeol.duckhang.chat.application.service.ChatUserService;

import java.util.Arrays;
import java.util.List;

@Component
public class WebSocketEventListener {
    private final ChatUserService chatUserService;

    public WebSocketEventListener(ChatUserService chatUserService) {
        this.chatUserService = chatUserService;
    }

    /**
     * 웹소켓 연결 시 실행되는 메서드
     * @param event 세션 connect event
     */
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(event.getMessage());;

        Object simpConnectMessage = stompHeaderAccessor.getHeader("simpConnectMessage");

        if (simpConnectMessage instanceof GenericMessage) {
            GenericMessage<?> connectMessage = (GenericMessage<?>) simpConnectMessage;

            StompHeaderAccessor connectAccessor = StompHeaderAccessor.wrap(connectMessage);
            List<String> uuidHeaders = connectAccessor.getNativeHeader("uuid");

            if (uuidHeaders != null && !uuidHeaders.isEmpty()) {
                String uuid = uuidHeaders.get(0);
                chatUserService.changeStatusToConnect(uuid);
            }
        }
    }

    /**
     * 웹소켓 연결 해제 시 실행되는 메서드
     * @param event session disconnect event
     */
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        System.out.println("disconnect handler");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String uuid = headerAccessor.getNativeHeader("uuid").get(0);

        chatUserService.changeStatusToDisconnect(uuid);
    }
}
