package oridungjeol.duckhang.board.application.port.out;

import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.BoardType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findById(Long id);
    List<Board> findAllByBoardType(BoardType boardType);
    void deleteById(Long id);
    List<Board> findAllByAuthorUuid(UUID authorUuid);
}
