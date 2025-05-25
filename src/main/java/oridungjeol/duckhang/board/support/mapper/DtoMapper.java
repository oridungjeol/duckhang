package oridungjeol.duckhang.board.support.mapper;

import oridungjeol.duckhang.board.application.dto.TradeDetailDto;
import oridungjeol.duckhang.board.application.dto.TradeListDto;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Purchase;
import oridungjeol.duckhang.user.infrastructure.entity.User;

public class DtoMapper {
    public static TradeListDto toTradeListDto(Board board, Purchase purchase, User user) {
        return TradeListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .imageUrl(board.getImageUrl())
                .price(purchase.getPrice())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static TradeDetailDto toTradeDetailDto(Board board, Purchase purchase, User user) {
        return TradeDetailDto.builder()
                .id(board.getId())
                .nickname(user.getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .price(purchase.getPrice())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
