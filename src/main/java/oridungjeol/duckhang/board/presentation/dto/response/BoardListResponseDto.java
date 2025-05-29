package oridungjeol.duckhang.board.presentation.dto.response;

import java.time.LocalDateTime;

public sealed interface BoardListResponseDto permits BoardListDto, TradeListDto, RentalListDto {
    LocalDateTime createdAt();
}
