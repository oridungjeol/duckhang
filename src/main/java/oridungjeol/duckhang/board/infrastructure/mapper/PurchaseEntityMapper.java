package oridungjeol.duckhang.board.infrastructure.mapper;

import oridungjeol.duckhang.board.domain.Purchase;
import oridungjeol.duckhang.board.infrastructure.entity.PurchaseEntity;

public class PurchaseEntityMapper {
    public static Purchase toDomain(PurchaseEntity entity) {
        return new Purchase(entity.getBoardId(), entity.getPrice());
    }

    public static PurchaseEntity toEntity(Purchase purchase) {
        return PurchaseEntity.builder()
                .boardId(purchase.getBoardId())
                .price(purchase.getPrice())
                .build();
    }
}
