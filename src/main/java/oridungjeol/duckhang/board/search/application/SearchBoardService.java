package oridungjeol.duckhang.board.search.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.search.domain.SearchBoardResultDto;
import oridungjeol.duckhang.board.search.infrastructure.SearchBoardRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SearchBoardService {

    private final SearchBoardRepository searchBoardRepository;

    public Page<SearchBoardResultDto> searchBoards(String keyword, Pageable pageable, BoardType boardType) {
        // searchFieldType 제거하고 호출
        return searchBoardRepository.searchBoard(keyword, pageable, Optional.ofNullable(boardType));
    }
}
