package oridungjeol.duckhang.board.infrastructure.mapper;

import oridungjeol.duckhang.board.domain.SellPost;
import oridungjeol.duckhang.board.infrastructure.entity.SellEntity;

public class SellEntityMapper {
    public static SellPost toDomain(SellEntity entity) {
        return new SellPost(entity.getBoardId(), entity.getPrice());
    }

    public static SellEntity toEntity(SellPost sellPost) {
        return SellEntity.builder()
                .boardId(sellPost.getBoardId())
                .price(sellPost.getPrice())
                .build();
    }
}
