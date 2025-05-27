package oridungjeol.duckhang.board.presentation.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record RentalListDto(
        Long id,
        String title,
        String imageUrl,
        int price,
        int deposit,
        LocalDateTime createdAt
) implements BoardListResponseDto {
}
