package oridungjeol.duckhang.board.application.mapper;

import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.presentation.dto.BoardDetailDto;
import oridungjeol.duckhang.board.presentation.dto.BoardListDto;
import oridungjeol.duckhang.board.presentation.dto.BoardListResponseDto;
import oridungjeol.duckhang.user.infrastructure.entity.User;

public class BoardDtoMapper {
    public static BoardListResponseDto toBoardListDto(Board board, User user) {
        return BoardListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .imageUrl(board.getImageUrl())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static BoardDetailDto toBoardDetailDto(Board board, User user) {
        return BoardDetailDto.builder()
                .id(board.getId())
                .author_uuid(board.getAuthorUuid())
                .nickname(user.getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .type(board.getBoardType())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
