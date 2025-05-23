package oridungjeol.duckhang.board.search.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import oridungjeol.duckhang.board.search.dto.BoardSearchResultDto;
import oridungjeol.duckhang.board.search.repository.BoardSearchRepository;
import oridungjeol.duckhang.board.search.service.BoardSearchService;
import oridungjeol.duckhang.board.support.enums.BoardType;



@RestController
@RequiredArgsConstructor
@RequestMapping("/board/search")
public class BoardSearchController {

    private final BoardSearchService boardSearchService;

    @GetMapping
    public Page<BoardSearchResultDto> searchBoards(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) BoardType boardType
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return boardSearchService.searchBoards(keyword, pageable, boardType);
    }


}
