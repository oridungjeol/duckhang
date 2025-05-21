package oridungjeol.duckhang.board.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.infrastructure.repository.DelegateJpaRepository;
import oridungjeol.duckhang.board.infrastructure.repository.RentalJpaRepository;
import oridungjeol.duckhang.payment.domain.PriceProvider;

@Component
@RequiredArgsConstructor
public class DelegatePriceProvider implements PriceProvider {

    private final DelegateJpaRepository delegateJpaRepository;

    @Override
    public boolean supports(String type) {
        return "DELEGATE".equalsIgnoreCase(type);
    }

    @Override
    public int getPrice(Long boardId) {
        var rental = delegateJpaRepository.findByBoardId(boardId)
                .orElseThrow(() -> new IllegalArgumentException("[대리] 게시글이 없습니다."));
        return rental.getPrice();
    }
}