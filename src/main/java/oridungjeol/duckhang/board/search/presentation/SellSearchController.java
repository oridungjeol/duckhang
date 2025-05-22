package oridungjeol.duckhang.board.search.presentation;

import lombok.RequiredArgsConstructor;
import oridungjeol.duckhang.board.search.dto.BoardSearchResultDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.board.search.service.SellSearchService;

@RestController
@RequestMapping("/search/sell")
@RequiredArgsConstructor
public class SellSearchController {

    private final SellSearchService sellSearchService;

    @GetMapping
    public Page<BoardSearchResultDto> searchSales(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        return sellSearchService.search(keyword, pageable);
    }
}
