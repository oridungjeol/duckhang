package oridungjeol.duckhang.board.application.port.out;

import oridungjeol.duckhang.board.domain.PurchasePost;

import java.util.Optional;

public interface PurchaseRepository {
    PurchasePost save(PurchasePost purchasePost);
    Optional<PurchasePost> findByBoardId(Long boardId);
    void deleteByBoardId(Long boardId);
}