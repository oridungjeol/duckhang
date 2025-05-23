package oridungjeol.duckhang.chat.infrastructure.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomPK {
    @Column(name = "room_id")
    private long room_id;

    @Column(name = "uuid")
    private String uuid;
}
