package oridungjeol.duckhang.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RentalRepository extends JpaRepository<RentalEntity, Integer> {
    // 기본 findById(int boardId) 사용
}