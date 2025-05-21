package oridungjeol.duckhang.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.infrastructure.repository.PurchaseJpaRepository;
import oridungjeol.duckhang.board.infrastructure.repository.RentalJpaRepository;
import oridungjeol.duckhang.payment.domain.PriceProvider;

@Component
@RequiredArgsConstructor
public class PurchasePriceProvider implements PriceProvider {

    private final PurchaseJpaRepository purchaseJpaRepository;

    @Override
    public boolean supports(String type) {
        return "PURCHASE".equalsIgnoreCase(type);
    }

    @Override
    public int getPrice(Long boardId) {
        var rental = purchaseJpaRepository.findByBoardId(boardId)
                .orElseThrow(() -> new IllegalArgumentException("[구매] 게시글이 없습니다."));
        return rental.getPrice();
    }
}