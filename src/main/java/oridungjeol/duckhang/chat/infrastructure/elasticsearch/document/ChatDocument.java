package oridungjeol.duckhang.chat.infrastructure.elasticsearch.document;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import oridungjeol.duckhang.chat.application.domain.MessageType;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName="chatting")
public class ChatDocument {
    @Id
    private Long id;

//    @Enumerated(EnumType.STRING) // enum 이름을 문자열로 DB에 저장
    private MessageType type;

    private String author_uuid;

    private String content;

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime created_at;

    private long room_id;

    public static ChatDocument toChatDocument(ChatEntity chatEntity) {
        return ChatDocument.builder()
                .id(chatEntity.getId())
                .type(chatEntity.getType())
                .author_uuid(chatEntity.getAuthor_uuid())
                .content(chatEntity.getContent())
                .created_at(chatEntity.getCreated_at())
                .room_id(chatEntity.getRoom_id())
                .build();
    }
}
