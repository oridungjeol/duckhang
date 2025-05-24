package oridungjeol.duckhang.chat.infrastructure.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ChatRoomParticipantPK {
    private long room_id;
    private String uuid;
}
