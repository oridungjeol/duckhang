package oridungjeol.duckhang.board.support.mapper;

import oridungjeol.duckhang.board.domain.Purchase;
import oridungjeol.duckhang.board.infrastructure.entity.PurchaseEntity;

public class PurchaseMapper {
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
