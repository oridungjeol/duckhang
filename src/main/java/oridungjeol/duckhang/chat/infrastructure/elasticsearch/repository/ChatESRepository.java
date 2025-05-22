package oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;

import java.util.List;

@Repository
public interface ChatESRepository extends ElasticsearchRepository<ChatDocument, Long> {
    @Query("{\"term\": {\"roomId\": ?0}}")
    List<ChatDocument> findChatByRoomId(long roomId);
}
