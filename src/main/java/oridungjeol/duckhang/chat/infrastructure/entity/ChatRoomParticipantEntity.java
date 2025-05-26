package oridungjeol.duckhang.chat.infrastructure.entity;

import jakarta.persistence.*;
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

    @Column(name = "name")
    private String name;

    @Column(name = "recent")
    private String recent;

    @Column(name = "board_id")
    private Long board_id;

    @Column(name = "type")
    private String type;
}
