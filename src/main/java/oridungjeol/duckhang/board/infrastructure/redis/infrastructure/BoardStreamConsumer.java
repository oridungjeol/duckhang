package oridungjeol.duckhang.board.infrastructure.redis.infrastructure;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import oridungjeol.duckhang.board.domain.BoardType;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.document.BoardDocument;
import oridungjeol.duckhang.board.infrastructure.elasticsearch.repository.BoardDocumentRepository;
import oridungjeol.duckhang.board.infrastructure.redis.support.BoardEventType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Executors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BoardStreamConsumer {

    private final BoardDocumentRepository boardDocumentRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STREAM_KEY = "board-stream";
    private static final String GROUP = "board-group";
    private static final String CONSUMER = "board-consumer";

    @EventListener(ApplicationReadyEvent.class)
    public void startListener() {
        Executors.newSingleThreadExecutor().submit(this::consume);
    }

    private void consume() {
        try {
            // 그룹 생성 (이미 존재하면 예외 발생 → 무시)
            redisTemplate.opsForStream().createGroup(STREAM_KEY, GROUP);
        } catch (Exception ignored) {
        }

        while (true) {
            List<MapRecord<String, Object, Object>> messages =
                    redisTemplate.opsForStream().read(
                            Consumer.from(GROUP, CONSUMER),
                            StreamReadOptions.empty().count(10).block(Duration.ofSeconds(2)),
                            StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed())
                    );

            if (messages != null) {
                for (MapRecord<String, Object, Object> message : messages) {
                    try {
                        Map<Object, Object> value = message.getValue();

                        BoardEventType eventType = BoardEventType.valueOf((String) value.get("eventType"));
                        Long boardId = Long.parseLong((String) value.get("id"));

                        if (eventType == BoardEventType.DELETE) {
                            boardDocumentRepository.deleteById(boardId);
                            log.info("🗑️ Elasticsearch 문서 삭제 완료: {}", boardId);
                        } else {
                            BoardDocument doc = BoardDocument.builder()
                                    .id(boardId)
                                    .authorUuid(UUID.fromString((String) value.get("authorUuid")))
                                    .title((String) value.get("title"))
                                    .content((String) value.get("content"))
                                    .imageUrl((String) value.get("imageUrl"))
                                    .createdAt(LocalDateTime.parse((String) value.get("createdAt")))
                                    .boardType(BoardType.valueOf((String) value.get("boardType")))
                                    .price(Integer.parseInt((String) value.get("price")))
                                    .deposit(Integer.parseInt((String) value.get("deposit")))
                                    .build();

                            boardDocumentRepository.save(doc);
                            log.info("✅ Elasticsearch 저장 완료: {}", doc.getId());
                        }

                        redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP, message.getId());

                    } catch (Exception e) {
                        log.error("❌ 메시지 처리 실패: {}", e.getMessage(), e);
                    }
                }
            }
        }
    }
}