package oridungjeol.duckhang.board.infrastructure.mapper;

import oridungjeol.duckhang.board.domain.PurchasePost;
import oridungjeol.duckhang.board.infrastructure.entity.PurchaseEntity;

public class PurchaseEntityMapper {
    public static PurchasePost toDomain(PurchaseEntity entity) {
        return new PurchasePost(entity.getBoardId(), entity.getPrice());
    }

    public static PurchaseEntity toEntity(PurchasePost purchasePost) {
        return PurchaseEntity.builder()
                .boardId(purchasePost.getBoardId())
                .price(purchasePost.getPrice())
                .build();
    }
}
