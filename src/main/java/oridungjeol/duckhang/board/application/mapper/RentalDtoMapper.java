package oridungjeol.duckhang.board.application.mapper;

import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.RentalPost;
import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.response.RentalDetailDto;
import oridungjeol.duckhang.board.presentation.dto.response.RentalListDto;
import oridungjeol.duckhang.user.infrastructure.entity.User;

public class RentalDtoMapper {
    public static BoardListResponseDto toRentalListDto(Board board, RentalPost rentalPost, User user) {
        return RentalListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .imageUrl(board.getImageUrl())
                .price(rentalPost.getPrice())
                .deposit(rentalPost.getDeposit())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static RentalDetailDto toRentalDetailDto(Board board, RentalPost rentalPost, User user) {
        return RentalDetailDto.builder()
                .id(board.getId())
                .author_uuid(board.getAuthorUuid())
                .nickname(user.getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .price(rentalPost.getPrice())
                .deposit(rentalPost.getDeposit())
                .type(board.getBoardType())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
