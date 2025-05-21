package oridungjeol.duckhang.chat.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class RedisOperator {
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Message Listener
     * consumer에서 메시지 실시간 감지하는 메소드
     * 메시지가 감지되면 onMessage 실행됨
     * @return
     */
    public StreamMessageListenerContainer createStreamMessageListenerContainer(){
        return StreamMessageListenerContainer.create(
                this.redisTemplate.getConnectionFactory(),
                StreamMessageListenerContainer
                        .StreamMessageListenerContainerOptions.builder()
                        .hashKeySerializer(new StringRedisSerializer())
                        .hashValueSerializer(new StringRedisSerializer())
                        .pollTimeout(Duration.ofMillis(20))
                        .build()
        );
    }
}
