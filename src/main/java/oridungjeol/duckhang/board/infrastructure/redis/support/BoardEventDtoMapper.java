package oridungjeol.duckhang.board.infrastructure.redis.support;

import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.PricedBoardDetail;
import oridungjeol.duckhang.board.infrastructure.redis.domain.BoardEventDto;

public class BoardEventDtoMapper {

    public static BoardEventDto toDto(Board board, BoardEventType type) {
        return BoardEventDto.builder()
                .id(board.getId())
                .authorUuid(board.getAuthorUuid())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .boardType(board.getBoardType())
                .createdAt(board.getCreatedAt())
                .eventType(type)
                .build();
    }

    public static BoardEventDto toDto(Board board, PricedBoardDetail detail, BoardEventType type) {
        return BoardEventDto.builder()
                .id(board.getId())
                .authorUuid(board.getAuthorUuid())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .boardType(board.getBoardType())
                .createdAt(board.getCreatedAt())
                .price(detail.getPrice())
                .eventType(type)
                .deposit(detail.getDeposit())
                .build();
    }
}
