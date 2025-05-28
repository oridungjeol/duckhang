package oridungjeol.duckhang.board.infrastructure.mapper;

import oridungjeol.duckhang.board.domain.RentalPost;
import oridungjeol.duckhang.board.infrastructure.entity.RentalEntity;

public class RentalEntityMapper
{
    public static RentalPost toDomain(RentalEntity entity) {
        return new RentalPost(entity.getBoardId(), entity.getPrice(), entity.getDeposit());
    }

    public static RentalEntity toEntity(RentalPost rentalPost) {
        return RentalEntity.builder()
                .boardId(rentalPost.getBoardId())
                .price(rentalPost.getPrice())
                .deposit(rentalPost.getDeposit())
                .build();
    }
}
