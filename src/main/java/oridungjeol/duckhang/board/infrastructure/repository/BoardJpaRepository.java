package oridungjeol.duckhang.board.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.board.infrastructure.entity.BoardEntity;
import oridungjeol.duckhang.board.support.enums.BoardType;

import java.util.List;

public interface BoardJpaRepository extends JpaRepository<BoardEntity, Long> {
    List<BoardEntity> findAllByBoardType(BoardType boardType);
}
