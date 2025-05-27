package oridungjeol.duckhang.board.infrastructure.redis.support;

import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.PricedBoardDetail;
import oridungjeol.duckhang.board.infrastructure.redis.domain.BoardEventDto;


public class BoardEventDtoMapper {

    public static BoardEventDto toDto(Board board, PricedBoardDetail detail, BoardEventType type) {
        return new BoardEventDto(
                board.getId(),
                board.getAuthorUuid(),
                board.getTitle(),
                board.getContent(),
                board.getImageUrl(),
                board.getBoardType(),
                board.getCreatedAt(),
                detail.getPrice(),
                type
        );
    }
}
