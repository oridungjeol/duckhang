package oridungjeol.duckhang.chat.infrastructure.redis.producer;

import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisStreamsChatProducer {
    public void saveInRedisStreams(RedisTemplate<String, String> redisTemplate, ChatEntity chatEntity) {
        Map<String, String> chatMap = new HashMap<>();
        chatMap.put("room_id", String.valueOf(chatEntity.getRoom_id()));
        chatMap.put("author_uuid", chatEntity.getAuthor_uuid());
        chatMap.put("content", chatEntity.getContent());
        chatMap.put("created_at", chatEntity.getCreated_at().toString());

        MapRecord<String, String, String> record = StreamRecords
                .newRecord()
                .in("chat-room:" + chatEntity.getRoom_id())
                .ofMap(chatMap);

        redisTemplate.opsForStream().add(record);
    }
}
