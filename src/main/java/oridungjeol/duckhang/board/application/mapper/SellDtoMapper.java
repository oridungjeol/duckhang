package oridungjeol.duckhang.board.application.mapper;

import oridungjeol.duckhang.board.presentation.dto.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.TradeDetailDto;
import oridungjeol.duckhang.board.presentation.dto.TradeListDto;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Sell;
import oridungjeol.duckhang.user.infrastructure.entity.User;

public class SellDtoMapper {
    public static BoardListResponseDto toTradeListDto(Board board, Sell sell, User user) {
        return TradeListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .imageUrl(board.getImageUrl())
                .price(sell.getPrice())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static TradeDetailDto toTradeDetailDto(Board board, Sell sell, User user) {
        return TradeDetailDto.builder()
                .id(board.getId())
                .author_uuid(board.getAuthorUuid())
                .nickname(user.getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .price(sell.getPrice())
                .type(board.getBoardType())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
