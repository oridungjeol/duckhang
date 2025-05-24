package oridungjeol.duckhang.chat.infrastructure.mapper;

import org.springframework.stereotype.Component;
import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;

@Component
public class ChatMapper {
    public Chat chatDocumentToDto(ChatDocument chatDocument) {
        return Chat.builder()
                .type(chatDocument.getType())
                .author_uuid(chatDocument.getAuthorUuid())
                .content(chatDocument.getContent())
                .created_at(chatDocument.getCreatedAt())
                .room_id(chatDocument.getRoomId())
                .build();
    }

    public static ChatDocument toChatDocument(Chat chat) {
        return ChatDocument.builder()
                .type(chat.getType())
                .authorUuid(chat.getAuthor_uuid())
                .content(chat.getContent())
                .createdAt(chat.getCreated_at())
                .roomId(chat.getRoom_id())
                .build();
    }
}
