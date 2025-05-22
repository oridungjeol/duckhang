package oridungjeol.duckhang.chat.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import oridungjeol.duckhang.chat.application.domain.MessageType;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;

import java.time.LocalDateTime;

@Getter
@Builder
public class Chat {
    private String id;

    private MessageType type;

    private String author_uuid;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;

    private long room_id;
}
