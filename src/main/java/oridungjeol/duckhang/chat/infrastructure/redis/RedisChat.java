package oridungjeol.duckhang.chat.infrastructure.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;

import java.util.HashMap;
import java.util.Map;

@Service
public class RedisChat {
    public void saveInRedisList(RedisTemplate<String, String> redisTemplate, ChatEntity chatEntity) throws JsonProcessingException {
        Map<String, String> chatMap = new HashMap<>();
        chatMap.put("type", String.valueOf(chatEntity.getType()));
        chatMap.put("room_id", String.valueOf(chatEntity.getRoom_id()));
        chatMap.put("author_uuid", chatEntity.getAuthor_uuid());
        chatMap.put("content", chatEntity.getContent());
        chatMap.put("created_at", chatEntity.getCreated_at().toString());

        ObjectMapper objectMapper = new ObjectMapper();
        String chatJson = objectMapper.writeValueAsString(chatMap);

        redisTemplate.opsForList().rightPush("recent-chat:" + chatEntity.getRoom_id(), chatJson);
//        redisTemplate.opsForList().trim("recent-chat:" + chatEntity.getRoom_id(), 0, 49);
    }
}
