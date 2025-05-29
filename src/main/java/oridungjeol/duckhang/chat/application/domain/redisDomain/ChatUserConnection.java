package oridungjeol.duckhang.chat.application.domain.redisDomain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 꼭 필요!
@AllArgsConstructor
@RedisHash(value = "status", timeToLive = 300)
public class ChatUserConnection {
    @Id
    private String uuid;
    private StatusType statusType;
}
