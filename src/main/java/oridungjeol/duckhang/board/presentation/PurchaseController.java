package oridungjeol.duckhang.board.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.board.application.dto.AllPurchaseDto;
import oridungjeol.duckhang.board.application.dto.PurchaseDetailDto;
import oridungjeol.duckhang.board.application.dto.PurchaseRequestDto;
import oridungjeol.duckhang.board.application.service.PurchaseService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/board/purchase")
@RequiredArgsConstructor
public class PurchaseController {
    private final PurchaseService purchaseService;

    @PostMapping
    public ResponseEntity<Long> createPurchase(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody PurchaseRequestDto purchaseRequestDto
    ) {
        UUID uuid = principal.getUuid();
        Long id = purchaseService.createPurchase(uuid, purchaseRequestDto);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public ResponseEntity<List<AllPurchaseDto>> findAll() {
        List<AllPurchaseDto> purchases = purchaseService.getAllPurchases();
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/{purchaseId}")
    public ResponseEntity<PurchaseDetailDto> findById(
            @PathVariable Long purchaseId
    ) {
        PurchaseDetailDto purchase = purchaseService.getPurchaseDetail(purchaseId);
        return ResponseEntity.ok(purchase);
    }

    @PatchMapping("/{purchaseId}")
    public ResponseEntity<Void> updatePurchase(
            @AuthenticationPrincipal String uuid,
            @PathVariable Long purchaseId,
            @RequestBody PurchaseRequestDto purchaseRequestDto
    ) {
        purchaseService.updatePurchase(UUID.fromString(uuid), purchaseId, purchaseRequestDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{purchaseId}")
    public ResponseEntity<Void> deletePurchase(
            @AuthenticationPrincipal String uuid,
            @PathVariable Long purchaseId
    ) {
        purchaseService.deletePurchase(UUID.fromString(uuid),purchaseId);
        return ResponseEntity.ok().build();
    }

}
