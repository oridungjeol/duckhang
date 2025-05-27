package oridungjeol.duckhang.board.search;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.search.repository.BoardSearchRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardSearchService {

    private final BoardSearchRepository boardSearchRepository;



    public Page<BoardSearchResultDto> searchBoards(String keyword, Pageable pageable, BoardType boardType) {
        return boardSearchRepository.searchBoard(keyword, pageable, Optional.ofNullable(boardType));
    }
}
