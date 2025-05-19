package oridungjeol.duckhang.board.application.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record PurchaseDetailDto(
        Long id,
        String nickname,
        String title,
        String content,
        String imageUrl,
        int price,
        LocalDateTime createdAt
){}