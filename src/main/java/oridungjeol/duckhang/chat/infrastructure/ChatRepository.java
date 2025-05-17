package oridungjeol.duckhang.chat.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, Integer> {
}
