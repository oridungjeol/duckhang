package oridungjeol.duckhang.chat.infrastructure.Entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "chatroom_participant")
public class ChatRoomParticipantEntity {
    @EmbeddedId
    ChatRoomParticipantPK participant_id;
}
