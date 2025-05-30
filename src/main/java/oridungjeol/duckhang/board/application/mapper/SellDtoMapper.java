package oridungjeol.duckhang.board.application.mapper;

import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.response.TradeDetailDto;
import oridungjeol.duckhang.board.presentation.dto.response.TradeListDto;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.SellPost;
import oridungjeol.duckhang.user.infrastructure.entity.User;

public class SellDtoMapper {
    public static BoardListResponseDto toTradeListDto(Board board, SellPost sellPost) {
        return TradeListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .imageUrl(board.getImageUrl())
                .price(sellPost.getPrice())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static TradeDetailDto toTradeDetailDto(Board board, SellPost sellPost, User user) {
        return TradeDetailDto.builder()
                .id(board.getId())
                .author_uuid(board.getAuthorUuid())
                .nickname(user.getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .price(sellPost.getPrice())
                .type(board.getBoardType())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
