package oridungjeol.duckhang.board.application.port.out;

import oridungjeol.duckhang.board.domain.Purchase;

import java.util.Optional;

public interface PurchaseRepository {
    Purchase save(Purchase purchase);
    Optional<Purchase> findByBoardId(Long boardId);
    void deleteByBoardId(Long boardId);
}