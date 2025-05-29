package oridungjeol.duckhang.board.infrastructure.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;

@Repository
public interface BoardDocumentRepository extends ElasticsearchRepository<BoardDocument, Long> {
}