package oridungjeol.duckhang.board.infrastructure.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.infrastructure.redis.domain.BoardEventDto;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BoardStreamPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publishBoard(BoardEventDto dto) {
        Map<String, String> message = new HashMap<>();
        message.put("id", String.valueOf(dto.getId()));
        message.put("authorUuid", dto.getAuthorUuid().toString());
        message.put("title", dto.getTitle());
        message.put("content", dto.getContent());
        message.put("imageUrl", dto.getImageUrl());
        message.put("createdAt", String.valueOf(dto.getCreatedAt()));
        message.put("boardType", String.valueOf(dto.getBoardType()));
        message.put("price", String.valueOf(dto.getPrice()));
        message.put("eventType" , String.valueOf(dto.getEventType()));
        message.put("deposit", String.valueOf(dto.getDeposit()));

        redisTemplate.opsForStream().add("board-stream", message);
    }
}
