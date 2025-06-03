package oridungjeol.duckhang.board.search.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.search.domain.SearchBoardResultDto;
import oridungjeol.duckhang.board.search.application.SearchBoardService;
import oridungjeol.duckhang.board.search.support.SearchFieldType;

@RestController
@RequiredArgsConstructor
@RequestMapping("/board/search")
public class SearchBoardController {

    private final SearchBoardService searchBoardService;

    @GetMapping
    public Page<SearchBoardResultDto> searchBoards(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) BoardType boardType
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return searchBoardService.searchBoards(keyword, pageable, boardType);
    }

}
