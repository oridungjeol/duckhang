package oridungjeol.duckhang.payment.presentation;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.payment.domain.PriceProvider;

import java.util.List;

@RestController
@RequestMapping("/api/price")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class PriceController {

    private final List<PriceProvider> priceProviders;

    @GetMapping("/{type}/{boardId}")
    public ResponseEntity<?> getPrice(
            @PathVariable String type,
            @PathVariable int boardId
    ) {
        return priceProviders.stream()
                .filter(p -> p.supports(type))
                .findFirst()
                .map(p -> ResponseEntity.ok().body(
                        java.util.Map.of("price", p.getPrice(boardId))
                ))
                .orElseThrow(() -> new IllegalArgumentException("지원하지 않는 타입입니다."));
    }
}