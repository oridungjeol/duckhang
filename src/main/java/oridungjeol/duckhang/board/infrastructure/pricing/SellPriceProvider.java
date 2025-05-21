package oridungjeol.duckhang.board.infrastructure.pricing;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.infrastructure.repository.SellJpaRepository;
import oridungjeol.duckhang.payment.domain.PriceProvider;

@Component
@RequiredArgsConstructor
public class SellPriceProvider implements PriceProvider {

    private final SellJpaRepository sellJapRepository;

    @Override
    public boolean supports(String type) {
        return "SELL".equalsIgnoreCase(type);
    }


    @Override
    public int getPrice(Long boardId) {
        return sellJapRepository.findByBoardId(boardId)
                .orElseThrow(() -> new IllegalArgumentException("[판매] 게시글이 없습니다."))
                .getPrice();
    }
}