package oridungjeol.duckhang.board.search.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.search.dto.BoardSearchResultDto;
import oridungjeol.duckhang.board.search.repository.BoardSearchRepository;
import oridungjeol.duckhang.board.support.enums.BoardType;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardSearchService {

    private final BoardSearchRepository boardSearchRepository;

    public Page<BoardSearchResultDto> searchBoards(String keyword, Pageable pageable, BoardType boardType) {
        return boardSearchRepository.searchBoard(keyword, pageable, Optional.ofNullable(boardType));
    }
}
