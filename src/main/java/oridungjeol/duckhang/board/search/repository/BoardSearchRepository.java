package oridungjeol.duckhang.board.search.repository;

import lombok.RequiredArgsConstructor;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.board.search.dto.BoardSearchResultDto;
import oridungjeol.duckhang.board.support.enums.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardSearchRepository {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public Page<BoardSearchResultDto> searchBoard(BoardType boardType, String keyword, Pageable pageable) {
        Criteria boardTypeCriteria = Criteria.where("boardType").is(boardType.name());
        Criteria keywordCriteria = new Criteria().or(Criteria.where("title").matches(keyword))
                .or(Criteria.where("content").matches(keyword));

        Criteria criteria = boardTypeCriteria.and(keywordCriteria);

        CriteriaQuery query = new CriteriaQuery(criteria, pageable);

        var searchHits = elasticsearchTemplate.search(query, BoardDocument.class);

        List<BoardSearchResultDto> results = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(doc -> BoardSearchResultDto.builder()
                        .id(doc.getId())
                        .title(doc.getTitle())
                        .content(doc.getContent())
                        .imageUrl(doc.getImageUrl())
                        .price(doc.getPrice())
//                        .createdAt(doc.getCreatedAt())
                        .boardType(doc.getBoardType())
                        .build())
                .toList();

        return new PageImpl<>(results, pageable, searchHits.getTotalHits());
    }
}
