package oridungjeol.duckhang.board.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.board.infrastructure.entity.SellEntity;

import java.util.Optional;

public interface DelegateJpaRepository extends JpaRepository<SellEntity, Integer> {
    Optional<SellEntity> findByBoardId(Long boardId);
}