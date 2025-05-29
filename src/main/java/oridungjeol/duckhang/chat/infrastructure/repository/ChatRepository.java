package oridungjeol.duckhang.chat.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomEntity;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatRoomParticipantEntity;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatRoomEntity, Long> {
    @Query("select c from ChatRoomEntity c where c.uuid = :uuid")
    List<ChatRoomEntity> findChatRoomByUuid(@Param("uuid") String uuid);

    @Query("select c from ChatRoomParticipantEntity c where c.participant_id.uuid = :uuid")
    List<ChatRoomParticipantEntity> findChatRoomParticipantByUuid(@Param("uuid") String uuid);
}
