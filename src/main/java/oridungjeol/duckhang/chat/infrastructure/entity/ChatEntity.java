package oridungjeol.duckhang.chat.infrastructure.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import oridungjeol.duckhang.chat.application.domain.MessageType;
import oridungjeol.duckhang.chat.application.dto.Chat;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatting")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ChatEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Enumerated(EnumType.STRING) // enum 이름을 문자열로 DB에 저장
    private MessageType type;

    private String author_uuid;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;

    private long room_id;
}
