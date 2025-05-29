package oridungjeol.duckhang.board.application.mapper;

import oridungjeol.duckhang.board.presentation.dto.response.BoardListResponseDto;
import oridungjeol.duckhang.board.presentation.dto.response.TradeDetailDto;
import oridungjeol.duckhang.board.presentation.dto.response.TradeListDto;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.PurchasePost;
import oridungjeol.duckhang.user.infrastructure.entity.User;

public class PurchaseDtoMapper {
    public static BoardListResponseDto toTradeListDto(Board board, PurchasePost purchasePost, User user) {
        return TradeListDto.builder()
                .id(board.getId())
                .title(board.getTitle())
                .imageUrl(board.getImageUrl())
                .price(purchasePost.getPrice())
                .createdAt(board.getCreatedAt())
                .build();
    }

    public static TradeDetailDto toTradeDetailDto(Board board, PurchasePost purchasePost, User user) {
        return TradeDetailDto.builder()
                .id(board.getId())
                .author_uuid(board.getAuthorUuid())
                .nickname(user.getNickname())
                .title(board.getTitle())
                .content(board.getContent())
                .imageUrl(board.getImageUrl())
                .price(purchasePost.getPrice())
                .type(board.getBoardType())
                .createdAt(board.getCreatedAt())
                .build();
    }
}
