package oridungjeol.duckhang.board.presentation.dto.response;

import lombok.Builder;
import oridungjeol.duckhang.board.domain.BoardType;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record RentalDetailDto(
        Long id,
        UUID author_uuid,
        String nickname,
        String title,
        String content,
        String imageUrl,
        int price,
        int deposit,
        BoardType type,
        LocalDateTime createdAt
) implements BoardResponseDto {
}
