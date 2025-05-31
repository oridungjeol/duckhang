package oridungjeol.duckhang.board.infrastructure.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.infrastructure.redis.support.BoardEventType;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class BoardEventDto {
    private Long id;
    private UUID authorUuid;
    private String title;
    private String content;
    private String imageUrl;
    private BoardType boardType;
    private LocalDateTime createdAt;
    private int price;
    private BoardEventType eventType;
    private int deposit;
}