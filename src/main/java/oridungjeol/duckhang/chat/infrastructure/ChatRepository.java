package oridungjeol.duckhang.chat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;

import java.awt.print.Pageable;
import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Long> {
    @Query(value = "select c " +
                    "from ChatEntity c " +
                    "where c.room_id=:room_id " +
                    "ORDER BY c.created_at")
    List<ChatEntity> findRecentChattingByRoom_id(@Param("room_id") long room_id);
}
