package oridungjeol.duckhang.board.presentation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.board.application.dto.TradeDetailDto;
import oridungjeol.duckhang.board.application.dto.TradeListDto;
import oridungjeol.duckhang.board.application.port.in.SellBoardUseCase;
import oridungjeol.duckhang.board.presentation.dto.RequestDto;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/board/sell")
@RequiredArgsConstructor
public class SellController {
    private final SellBoardUseCase sellBoardUseCase;

    @PostMapping
    public ResponseEntity<Long> createSell(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody RequestDto requestDto
    ) {
        String uuid = principal.getName();
        Long id = sellBoardUseCase.createBoard(UUID.fromString(uuid), requestDto);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public ResponseEntity<List<TradeListDto>> findAll() {
        List<TradeListDto> sells = sellBoardUseCase.getAllBoards();
        return ResponseEntity.ok(sells);
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<TradeDetailDto> findById(
            @PathVariable Long purchaseId
    ) {
        TradeDetailDto purchase = sellBoardUseCase.getDetailBoard(purchaseId);
        return ResponseEntity.ok(purchase);
    }

    @PatchMapping("/{purchaseId}")
    public ResponseEntity<Long> updateSell(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable Long purchaseId,
            @RequestBody RequestDto requestDto
    ) {
        String uuid = principal.getName();
        Long updatedId = sellBoardUseCase.updateBoard( purchaseId, UUID.fromString(uuid), requestDto);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<Void> deleteSell(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable Long purchaseId
    ) {
        String uuid = principal.getName();
        sellBoardUseCase.deleteBoard(UUID.fromString(uuid),purchaseId);
        return ResponseEntity.ok().build();
    }
}
