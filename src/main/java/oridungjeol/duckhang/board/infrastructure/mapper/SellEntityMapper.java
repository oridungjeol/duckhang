package oridungjeol.duckhang.board.infrastructure.mapper;

import oridungjeol.duckhang.board.domain.Sell;
import oridungjeol.duckhang.board.infrastructure.entity.SellEntity;

public class SellEntityMapper {
    public static Sell toDomain(SellEntity entity) {
        return new Sell(entity.getBoardId(), entity.getPrice());
    }

    public static SellEntity toEntity(Sell sell) {
        return SellEntity.builder()
                .boardId(sell.getBoardId())
                .price(sell.getPrice())
                .build();
    }
}
