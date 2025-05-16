package oridungjeol.duckhang.board.rental;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.board.sell.SellEntity;

import java.util.Optional;

public interface RentalRepository extends JpaRepository<RentalEntity, Integer> {
    Optional<RentalEntity> findByBoardId(int boardId);
}