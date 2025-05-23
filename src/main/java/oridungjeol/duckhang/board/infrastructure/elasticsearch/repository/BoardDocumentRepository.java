package oridungjeol.duckhang.board.infrastructure.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.board.support.enums.BoardType;

import java.util.List;

public interface BoardDocumentRepository extends ElasticsearchRepository<BoardDocument, Long> {
}
