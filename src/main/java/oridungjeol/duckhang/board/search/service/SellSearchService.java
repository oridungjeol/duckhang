package oridungjeol.duckhang.board.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.search.dto.BoardSearchResultDto;
import oridungjeol.duckhang.board.search.repository.BoardSearchRepository;
import oridungjeol.duckhang.board.support.enums.BoardType;

@Service
@RequiredArgsConstructor
public class SellSearchService {

    private final BoardSearchRepository boardSearchRepository;

    public Page<BoardSearchResultDto> search(String keyword, Pageable pageable) {
        return boardSearchRepository.searchBoard(BoardType.SELL, keyword, pageable);
    }
}