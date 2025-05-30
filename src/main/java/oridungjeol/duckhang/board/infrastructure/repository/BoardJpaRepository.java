package oridungjeol.duckhang.board.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.board.infrastructure.entity.BoardEntity;
import oridungjeol.duckhang.board.domain.BoardType;

import java.util.List;
import java.util.UUID;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findAllByBoardType(BoardType boardType);
    List<BoardEntity> findAllByAuthorUuid(UUID authorUuid);
}
