package oridungjeol.duckhang.chat.infrastructure.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.chat.infrastructure.elasticsearch.document.ChatDocument;

@Repository
public interface ChatESRepository extends ElasticsearchRepository<ChatDocument, Long> {
}
