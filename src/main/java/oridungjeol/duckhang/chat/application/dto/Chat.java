package oridungjeol.duckhang.chat.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import oridungjeol.duckhang.chat.application.domain.MessageType;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;

import java.time.LocalDateTime;

@Getter
@Setter
public class Chat {
    private int id;

    private String type;

    private String author_uuid;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;

    private int room_id;

    public ChatEntity toEntity() {
        return ChatEntity.builder()
                .type(MessageType.valueOf(type.toUpperCase()))
                .author_uuid(author_uuid)
                .content(content)
                .created_at(created_at)
                .room_id(room_id)
                .build();
    }
}
