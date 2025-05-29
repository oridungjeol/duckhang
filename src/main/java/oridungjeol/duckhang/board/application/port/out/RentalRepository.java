package oridungjeol.duckhang.board.application.port.out;

import oridungjeol.duckhang.board.domain.RentalPost;
import java.util.Optional;

public interface RentalRepository {
    RentalPost save(RentalPost rentalPost);
    Optional<RentalPost> findByBoardId(Long boardId);
    void deleteByBoardId(Long boardId);
}
