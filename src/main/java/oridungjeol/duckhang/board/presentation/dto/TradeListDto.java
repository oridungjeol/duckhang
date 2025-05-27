package oridungjeol.duckhang.board.presentation.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TradeListDto(
    Long id,
    String title,
    String imageUrl,
    int price,
    LocalDateTime createdAt
) implements BoardListResponseDto{}
