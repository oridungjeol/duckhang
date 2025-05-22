package oridungjeol.duckhang.board.search.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import oridungjeol.duckhang.board.search.dto.BoardSearchResultDto;
import oridungjeol.duckhang.board.search.service.PurchaseSearchService;
import oridungjeol.duckhang.board.search.service.SellSearchService;

@RestController
@RequestMapping("/search/purchase")
@RequiredArgsConstructor
public class PurchaseSearchController {

    private final PurchaseSearchService purchaseSearchService;

    @GetMapping
    public Page<BoardSearchResultDto> searchSales(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        return purchaseSearchService.search(keyword, pageable);
    }
}
