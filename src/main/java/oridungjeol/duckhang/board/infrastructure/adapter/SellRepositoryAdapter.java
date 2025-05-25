package oridungjeol.duckhang.board.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.application.port.out.SellRepository;
import oridungjeol.duckhang.board.domain.Sell;
import oridungjeol.duckhang.board.infrastructure.entity.SellEntity;
import oridungjeol.duckhang.board.infrastructure.mapper.SellEntityMapper;
import oridungjeol.duckhang.board.infrastructure.repository.SellJpaRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SellRepositoryAdapter implements SellRepository {
    private final SellJpaRepository sellJpaRepository;
    @Override
    public Sell save(Sell sell){
        SellEntity entity = SellEntityMapper.toEntity(sell);
        return SellEntityMapper.toDomain(sellJpaRepository.save(entity));
    }

    @Override
    public Optional<Sell> findByBoardId(Long boardId) {
        return sellJpaRepository.findByBoardId(boardId)
                .map(SellEntityMapper::toDomain);
    }

    @Override
    public void deleteByBoardId(Long boardId) {
        sellJpaRepository.findByBoardId(boardId)
                .ifPresent(sellJpaRepository::delete);
    }
}
