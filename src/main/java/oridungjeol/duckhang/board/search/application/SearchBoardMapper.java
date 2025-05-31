package oridungjeol.duckhang.board.search.application;

import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.board.search.domain.SearchBoardResultDto;

@Component
public class SearchBoardMapper {
    public SearchBoardResultDto toDto(BoardDocument doc) {
        return SearchBoardResultDto.builder()
                .id(doc.getId())
                .title(doc.getTitle())
                .content(doc.getContent())
                .imageUrl(doc.getImageUrl())
                .price(doc.getPrice())
                .boardType(doc.getBoardType())
                .createdAt(doc.getCreatedAt())
                .build();
    }
}
