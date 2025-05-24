package oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;

import java.util.List;

@Repository
public interface ChatESRepository extends ElasticsearchRepository<ChatDocument, Long> {
    @Query("{\"term\": {\"roomId\": ?0}}")
    Page<ChatDocument> findChatByRoomId(long roomId, Pageable pageable);
}
