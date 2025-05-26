package oridungjeol.duckhang.board.presentation.dto;

import lombok.Builder;
import oridungjeol.duckhang.board.domain.BoardType;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record BoardDetailDto(
        Long id,
        UUID author_uuid,
        String nickname,
        String title,
        String content,
        String imageUrl,
        BoardType type,
        LocalDateTime createdAt
) implements BoardResponseDto {
}
