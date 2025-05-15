package oridungjeol.duckhang.board.rental;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.payment.domain.PriceProvider;

@Component
@RequiredArgsConstructor
public class RentalPriceProvider implements PriceProvider {

    private final RentalRepository rentalRepository;

    @Override
    public boolean supports(String type) {
        return "RENTAL".equalsIgnoreCase(type);
    }

    @Override
    public int getPrice(int boardId) {
        return rentalRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("렌탈 글이 없습니다."))
                .getPrice();
    }
}