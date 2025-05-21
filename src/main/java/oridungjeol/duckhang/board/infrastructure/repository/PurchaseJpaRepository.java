package oridungjeol.duckhang.board.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.board.infrastructure.entity.PurchaseEntity;

import java.util.Optional;

public interface PurchaseJpaRepository extends JpaRepository<PurchaseEntity,Long> {
    Optional<PurchaseEntity> findByBoardId(Long boardId);
}
