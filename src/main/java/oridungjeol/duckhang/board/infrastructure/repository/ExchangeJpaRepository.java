package oridungjeol.duckhang.board.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.board.infrastructure.entity.ExchangeEntity;
import oridungjeol.duckhang.board.infrastructure.entity.SellEntity;

import java.util.Optional;

public interface ExchangeJpaRepository extends JpaRepository<ExchangeEntity, Integer> {
    Optional<ExchangeEntity> findByBoardId(Long boardId);
}