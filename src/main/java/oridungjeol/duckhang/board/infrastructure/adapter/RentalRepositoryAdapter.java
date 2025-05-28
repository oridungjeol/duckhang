package oridungjeol.duckhang.board.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.application.port.out.RentalRepository;
import oridungjeol.duckhang.board.domain.RentalPost;
import oridungjeol.duckhang.board.infrastructure.entity.RentalEntity;
import oridungjeol.duckhang.board.infrastructure.mapper.RentalEntityMapper;
import oridungjeol.duckhang.board.infrastructure.repository.RentalJpaRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RentalRepositoryAdapter implements RentalRepository {
    private final RentalJpaRepository rentalJpaRepository;
    @Override
    public RentalPost save(RentalPost rentalPost) {
        RentalEntity entity = RentalEntityMapper.toEntity(rentalPost);
        return RentalEntityMapper.toDomain(rentalJpaRepository.save(entity));
    }

    @Override
    public Optional<RentalPost> findByBoardId(Long boardId) {
        return rentalJpaRepository.findByBoardId(boardId)
                .map(RentalEntityMapper::toDomain);
    }

    @Override
    public void deleteByBoardId(Long boardId) {
        rentalJpaRepository.findByBoardId(boardId)
                .ifPresent(rentalJpaRepository::delete);
    }
}
