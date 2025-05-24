package oridungjeol.duckhang.chat.infrastructure.mapper;

import org.springframework.stereotype.Component;
import oridungjeol.duckhang.chat.application.dto.ChatRoom;
import oridungjeol.duckhang.chat.application.dto.ChatRoomParticipant;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomEntity;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomParticipantEntity;

@Component
public class ChatRoomMapper {
    /**
     * chatroomEntity를 chatroomDto로 변환
     * @param chatRoomEntity
     * @return
     */
    public ChatRoom chatRoomToDto(ChatRoomEntity chatRoomEntity) {
        return ChatRoom.builder()
                .room_id(chatRoomEntity.getRoom_id())
                .uuid(chatRoomEntity.getUuid())
                .name(chatRoomEntity.getName())
                .recent(chatRoomEntity.getRecent())
                .board_id(chatRoomEntity.getBoard_id())
                .type(chatRoomEntity.getType())
                .build();
    }

    /**
     * chatroomparticipantEntity를 chatroomDto로 변환
     * @param chatRoomParticipantEntity
     * @return
     */
    public ChatRoom chatRoomParticipantToDto(ChatRoomParticipantEntity chatRoomParticipantEntity) {
        return ChatRoom.builder()
                .room_id(chatRoomParticipantEntity.getParticipant_id().getRoom_id())
                .uuid(chatRoomParticipantEntity.getParticipant_id().getUuid())
                .name(chatRoomParticipantEntity.getName())
                .recent(chatRoomParticipantEntity.getRecent())
                .board_id(chatRoomParticipantEntity.getBoard_id())
                .type(chatRoomParticipantEntity.getType())
                .build();
    }
}
