package oridungjeol.duckhang.board.application.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record TradeListDto(
    Long id,
    String nickname,
    String title,
    String imageUrl,
    int price,
    LocalDateTime createdAt
) {}
