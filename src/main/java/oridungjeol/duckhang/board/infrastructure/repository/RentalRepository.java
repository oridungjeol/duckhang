package oridungjeol.duckhang.board.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.board.infrastructure.entity.RentalEntity;

import java.util.Optional;

public interface RentalRepository extends JpaRepository<RentalEntity, Integer> {
    Optional<RentalEntity> findByBoardId(int boardId);
}