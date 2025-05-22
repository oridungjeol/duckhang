package oridungjeol.duckhang.board.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.board.infrastructure.entity.HelperEntity;
import oridungjeol.duckhang.board.infrastructure.entity.SellEntity;

import java.util.Optional;

public interface HelperJpaRepository extends JpaRepository<HelperEntity, Integer> {
    Optional<HelperEntity> findByBoardId(Long boardId);
}