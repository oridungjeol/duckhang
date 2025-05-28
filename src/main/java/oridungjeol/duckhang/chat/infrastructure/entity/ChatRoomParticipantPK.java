package oridungjeol.duckhang.chat.infrastructure.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ChatRoomParticipantPK {
    private long room_id;
    private String uuid;
}
