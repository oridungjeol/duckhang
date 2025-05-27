package oridungjeol.duckhang.board.infrastructure.redis;

import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Purchase;


public class BoardEventDtoMapper {

    public static BoardEventDto toDto(Board board, Purchase purchase) {
        return new BoardEventDto(
                board.getId(),
                board.getAuthorUuid(),
                board.getTitle(),
                board.getContent(),
                board.getImageUrl(),
                board.getBoardType(),
                board.getCreatedAt(),
                purchase.getPrice()
        );
    }
}
