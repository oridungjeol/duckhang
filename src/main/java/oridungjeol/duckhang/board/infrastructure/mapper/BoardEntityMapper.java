package oridungjeol.duckhang.board.infrastructure.mapper;

import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.infrastructure.entity.BoardEntity;

import java.time.LocalDateTime;

public class BoardEntityMapper {
    public static Board toDomain(BoardEntity entity) {
        return new Board(
                entity.getId(),
                entity.getAuthorUuid(),
                entity.getTitle(),
                entity.getContent(),
                entity.getImageUrl(),
                entity.getBoardType(),
                entity.getCreatedAt()
        );
    }

    public static BoardEntity toEntity(Board board) {
        return BoardEntity.builder()
                .authorUuid(board.getAuthorUuid())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .boardType(board.getBoardType())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static BoardEntity toUpdatedEntity(BoardEntity entity, Board board) {
        return entity.toBuilder()
                .authorUuid(board.getAuthorUuid())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .boardType(board.getBoardType())
                .build();
    }
}