package oridungjeol.duckhang.board.search.infrastructure;

import lombok.RequiredArgsConstructor;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.board.search.domain.SearchBoardResultDto;
import oridungjeol.duckhang.board.search.support.SearchFieldType;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SearchBoardRepository {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public Page<SearchBoardResultDto> searchBoard(
            String keyword,
            Pageable pageable,
            Optional<BoardType> boardType,
            SearchFieldType fieldType
    ) {
        Criteria criteria = new Criteria();

        if (keyword != null && !keyword.isBlank()) {
            Criteria keywordCriteria;

            switch (fieldType) {
                case TITLE -> keywordCriteria = Criteria.where("title").matches(keyword);
                case CONTENT -> keywordCriteria = Criteria.where("content").matches(keyword);
                case ALL -> keywordCriteria = new Criteria()
                        .or(Criteria.where("title").matches(keyword))
                        .or(Criteria.where("content").matches(keyword));
                default -> throw new IllegalArgumentException("Invalid field type");
            }

            criteria = criteria.and(keywordCriteria);
        }

        if (boardType.isPresent()) {
            // .keyword 필드를 명시적으로 사용하여 정확한 매칭 수행
            criteria = criteria.and(Criteria.where("boardType.keyword").is(boardType.get().name()));
        }

        CriteriaQuery query = new CriteriaQuery(criteria, pageable);

        var searchHits = elasticsearchTemplate.search(query, BoardDocument.class);

        List<SearchBoardResultDto> results = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .map(doc -> SearchBoardResultDto.builder()
                        .id(doc.getId())
                        .title(doc.getTitle())
                        .content(doc.getContent())
                        .imageUrl(doc.getImageUrl())
                        .price(doc.getPrice())
                        .boardType(doc.getBoardType())
                        .createdAt(doc.getCreatedAt())
                        .build())
                .toList();

        return new PageImpl<>(results, pageable, searchHits.getTotalHits());
    }
}
