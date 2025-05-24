package oridungjeol.duckhang.board.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.board.application.dto.RequestDto;
import oridungjeol.duckhang.board.application.dto.TradeDetailDto;
import oridungjeol.duckhang.board.application.dto.TradeListDto;
import oridungjeol.duckhang.board.application.service.SellService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/board/sell")
@RequiredArgsConstructor
public class SellController {
    private final SellService sellService;

    @PostMapping
    public ResponseEntity<Long> createsell(
            @AuthenticationPrincipal CustomPrincipal principal,
            @RequestBody RequestDto requestDto
    ) {
        String uuid = principal.getName();
        Long id = sellService.createSell(UUID.fromString(uuid), requestDto);
        return ResponseEntity.ok(id);
    }


    @GetMapping
    public ResponseEntity<List<TradeListDto>> findAll() {
        List<TradeListDto> sells = sellService.getAllsells();
        return ResponseEntity.ok(sells);
    }

    @GetMapping("/{sellId}")
    public ResponseEntity<TradeDetailDto> findById(
            @PathVariable Long sellId
    ) {
        TradeDetailDto sell = sellService.getsellDetail(sellId);
        return ResponseEntity.ok(sell);
    }

    @PatchMapping("/{sellId}")
    public ResponseEntity<Long> updatesell(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable Long sellId,
            @RequestBody RequestDto requestDto
    ) {
        String uuid = principal.getName();
        Long updatedId = sellService.updatesell(UUID.fromString(uuid), sellId, requestDto);
        return ResponseEntity.ok(updatedId);
    }

    @DeleteMapping("/{sellId}")
    public ResponseEntity<Void> deletesell(
            @AuthenticationPrincipal CustomPrincipal principal,
            @PathVariable Long sellId
    ) {
        String uuid = principal.getName();
        sellService.deletesell(UUID.fromString(uuid),sellId);
        return ResponseEntity.ok().build();
    }

}
