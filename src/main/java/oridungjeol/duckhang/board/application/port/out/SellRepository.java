package oridungjeol.duckhang.board.application.port.out;

import oridungjeol.duckhang.board.domain.SellPost;

import java.util.Optional;

public interface SellRepository {
    SellPost save(SellPost sellPost);
    Optional<SellPost> findByBoardId(Long boardId);
    void deleteByBoardId(Long boardId);
}
