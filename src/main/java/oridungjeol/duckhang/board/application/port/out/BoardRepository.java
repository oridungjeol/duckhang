package oridungjeol.duckhang.board.application.port.out;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.BoardType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoardRepository {
    Board save(Board board);
    Optional<Board> findById(Long id);
    Page<Board> findAllByBoardType(BoardType boardType, Pageable pageable);
    void deleteById(Long id);
    List<Board> findAllByAuthorUuid(UUID authorUuid);
}
