package oridungjeol.duckhang.board.infrastructure.pricing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.infrastructure.repository.RentalJpaRepository;
import oridungjeol.duckhang.payment.domain.PriceProvider;

@Component
@RequiredArgsConstructor
public class RentalPriceProvider implements PriceProvider {

    private final RentalJpaRepository rentalJpaRepository;

    @Override
    public boolean supports(String type) {
        return "RENTAL".equalsIgnoreCase(type);
    }

    @Override
    public int getPrice(Long boardId) {
        var rental = rentalJpaRepository.findByBoardId(boardId)
                .orElseThrow(() -> new IllegalArgumentException("[대여] 게시글이 없습니다."));
        return rental.getPrice() + rental.getDeposit();
    }
}