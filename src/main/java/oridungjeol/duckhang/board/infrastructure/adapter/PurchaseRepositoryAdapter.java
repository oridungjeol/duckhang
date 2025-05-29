package oridungjeol.duckhang.board.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.application.port.out.PurchaseRepository;
import oridungjeol.duckhang.board.domain.PurchasePost;
import oridungjeol.duckhang.board.infrastructure.entity.PurchaseEntity;
import oridungjeol.duckhang.board.infrastructure.repository.PurchaseJpaRepository;
import oridungjeol.duckhang.board.infrastructure.mapper.PurchaseEntityMapper;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PurchaseRepositoryAdapter implements PurchaseRepository {
    private final PurchaseJpaRepository purchaseJpaRepository;
    @Override
    public PurchasePost save(PurchasePost purchasePost) {
        PurchaseEntity entity = PurchaseEntityMapper.toEntity(purchasePost);
        return PurchaseEntityMapper.toDomain(purchaseJpaRepository.save(entity));
    }

    @Override
    public Optional<PurchasePost> findByBoardId(Long boardId) {
        return purchaseJpaRepository.findByBoardId(boardId)
                .map(PurchaseEntityMapper::toDomain);
    }

    @Override
    public void deleteByBoardId(Long boardId) {
        purchaseJpaRepository.findByBoardId(boardId)
                .ifPresent(purchaseJpaRepository::delete);
    }
}
