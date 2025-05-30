package oridungjeol.duckhang.chat.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomEntity;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomParticipantEntity;

@Repository
public interface ChatParticipantRepository extends JpaRepository<ChatRoomParticipantEntity, Long> {
    @Query("select c from ChatRoomParticipantEntity c where c.participant_id.uuid = :uuid and c.board_id = :board_id")
    ChatRoomParticipantEntity findChatRoomIsExist(@Param("uuid") String uuid, @Param("board_id") long board_id);
}
