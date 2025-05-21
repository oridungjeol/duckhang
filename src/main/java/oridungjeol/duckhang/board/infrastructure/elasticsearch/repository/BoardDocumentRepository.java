package oridungjeol.duckhang.board.infrastructure.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;

public interface BoardDocumentRepository extends ElasticsearchRepository<BoardDocument, Long> {
}
