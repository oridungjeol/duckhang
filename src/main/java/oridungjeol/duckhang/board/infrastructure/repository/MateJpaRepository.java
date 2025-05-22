package oridungjeol.duckhang.board.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.board.infrastructure.entity.MateEntity;
import oridungjeol.duckhang.board.infrastructure.entity.SellEntity;

import java.util.Optional;

public interface MateJpaRepository extends JpaRepository<MateEntity, Integer> {
    Optional<MateEntity> findByBoardId(Long boardId);
}