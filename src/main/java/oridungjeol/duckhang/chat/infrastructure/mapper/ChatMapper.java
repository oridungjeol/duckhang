package oridungjeol.duckhang.chat.infrastructure.mapper;

import oridungjeol.duckhang.chat.application.dto.Chat;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;

public class ChatMapper {
    public ChatEntity chatToEntity(Chat chat) {
        return ChatEntity.builder()
                .type(chat.getType())
                .author_uuid(chat.getAuthor_uuid())
                .content(chat.getContent())
                .created_at(chat.getCreated_at())
                .room_id(chat.getRoom_id())
                .build();
    }

    public Chat chatEntityToDto(ChatEntity chatEntity) {
        return Chat.builder()
                .type(chatEntity.getType())
                .author_uuid(chatEntity.getAuthor_uuid())
                .content(chatEntity.getContent())
                .created_at(chatEntity.getCreated_at())
                .room_id(chatEntity.getRoom_id())
                .build();
    }

    public Chat chatDocumentToDto(ChatDocument chatDocument) {
        return Chat.builder()
                .type(chatDocument.getType())
                .author_uuid(chatDocument.getAuthor_uuid())
                .content(chatDocument.getContent())
                .created_at(chatDocument.getCreated_at())
                .room_id(chatDocument.getRoom_id())
                .build();
    }
}
