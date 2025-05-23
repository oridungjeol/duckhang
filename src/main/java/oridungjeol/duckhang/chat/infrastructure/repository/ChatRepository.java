package oridungjeol.duckhang.chat.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.chat.infrastructure.Entity.ChatRoomEntity;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatRoomEntity, Long> {
    @Query("select c.room_id from ChatRoomEntity c where c.uuid = :uuid")
    Long findChatRoomByUuid(@Param("uuid") String uuid);
}
