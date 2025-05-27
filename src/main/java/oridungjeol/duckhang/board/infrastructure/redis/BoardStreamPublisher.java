package oridungjeol.duckhang.board.infrastructure.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.board.domain.Board;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BoardStreamPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publishBoard(Board board) {
        Map<String, String> message = new HashMap<>();
        message.put("id", String.valueOf(board.getId()));
        message.put("authorUuid", board.getAuthorUuid().toString());
        message.put("title", board.getTitle());
        message.put("content", board.getContent());
        message.put("imageUrl", board.getImageUrl());
        message.put("createdAt", board.getCreatedAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        message.put("boardType", board.getBoardType().name());

        redisTemplate.opsForStream().add("board-stream", message);
    }
}
