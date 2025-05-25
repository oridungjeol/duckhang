package oridungjeol.duckhang.board.application.port.out;

import oridungjeol.duckhang.board.domain.Sell;

import java.util.Optional;

public interface SellRepository {
    Sell save(Sell sell);
    Optional<Sell> findByBoardId(Long boardId);
    void deleteByBoardId(Long boardId);
}
