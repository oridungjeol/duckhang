package oridungjeol.duckhang.board.sell;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellRepository extends JpaRepository<SellEntity, Integer> {
    Optional<SellEntity> findByBoardId(int boardId);
}