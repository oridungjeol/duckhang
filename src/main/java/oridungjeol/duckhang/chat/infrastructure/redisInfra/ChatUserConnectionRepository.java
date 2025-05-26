package oridungjeol.duckhang.chat.infrastructure.redisInfra;

import org.springframework.data.repository.CrudRepository;
import oridungjeol.duckhang.chat.application.domain.redisDomain.ChatUserConnection;

public interface ChatUserConnectionRepository extends CrudRepository<ChatUserConnection, String> {
}
