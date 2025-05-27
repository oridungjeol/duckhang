package oridungjeol.duckhang.board.infrastructure.redis;

import lombok.AllArgsConstructor;
import lombok.Getter;
import oridungjeol.duckhang.board.domain.BoardType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class BoardEventDto {
    private Long id;
    private UUID authorUuid;
    private String title;
    private String content;
    private String imageUrl;
    private BoardType boardType;
    private LocalDateTime createdAt;
    private int price;
}