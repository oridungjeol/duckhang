package oridungjeol.duckhang.board.application.port.out;

import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.support.enums.BoardType;

import java.util.List;
import java.util.Optional;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findById(Long id);
    List<Board> findAllByBoardType(BoardType boardType);
    void deleteById(Long id);
}
