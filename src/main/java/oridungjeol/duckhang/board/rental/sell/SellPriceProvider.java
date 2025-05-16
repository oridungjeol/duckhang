package oridungjeol.duckhang.board.rental.sell;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.payment.domain.PriceProvider;

@Component
@RequiredArgsConstructor
public class SellPriceProvider implements PriceProvider {

    private final SellRepository sellRepository;

    @Override
    public boolean supports(String type) {
        return "SELL".equalsIgnoreCase(type);
    }

    @Override
    public int getPrice(int boardId) {
        return sellRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("[판매] 게시글이 없습니다."))
                .getPrice();
    }
}