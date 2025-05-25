package oridungjeol.duckhang.board.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.board.application.dto.RequestDto;
import oridungjeol.duckhang.board.application.dto.TradeDetailDto;
import oridungjeol.duckhang.board.application.dto.TradeListDto;
import oridungjeol.duckhang.board.application.port.in.PurchaseBoardUseCase;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/board/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseBoardUseCase purchaseBoardUseCase;

    @PostMapping
    public ResponseEntity<Long> createPurchase(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody RequestDto requestDto
    ) {
        String uuid = principal.getName();
        Long id = purchaseBoardUseCase.createBoard(UUID.fromString(uuid), requestDto);
        return ResponseEntity.ok(id);
    }


    @GetMapping
    public ResponseEntity<List<TradeListDto>> findAll() {
        List<TradeListDto> purchases = purchaseBoardUseCase.getAllBoards();
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<TradeDetailDto> findById(
            @PathVariable Long purchaseId
    ) {
        TradeDetailDto purchase = purchaseBoardUseCase.getDetailBoard(purchaseId);
        return ResponseEntity.ok(purchase);
    }

    @PatchMapping("/{purchaseId}")
    public ResponseEntity<Long> updatePurchase(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable Long purchaseId,
            @RequestBody RequestDto requestDto
    ) {
        String uuid = principal.getName();
        Long updatedId = purchaseBoardUseCase.updateBoard( purchaseId, UUID.fromString(uuid), requestDto);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<Void> deletePurchase(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable Long purchaseId
    ) {
        String uuid = principal.getName();
        purchaseBoardUseCase.deleteBoard(UUID.fromString(uuid),purchaseId);
        return ResponseEntity.ok().build();
    }

}
