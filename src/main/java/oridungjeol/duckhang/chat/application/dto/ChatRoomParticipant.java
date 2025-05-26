package oridungjeol.duckhang.chat.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomParticipant {
    private Long room_id;

    private String uuid;

    private String name;

    private String recent;

    private Long board_id;

    private String type;
}
