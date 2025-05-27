package oridungjeol.duckhang.board.presentation.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record BoardListDto(
        Long id,
        String title,
        String imageUrl,
        LocalDateTime createdAt
) implements BoardListResponseDto{
}
