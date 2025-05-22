//package oridungjeol.duckhang.chat.infrastructure.redis.consumer;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.DisposableBean;
//import org.springframework.beans.factory.InitializingBean;
//import org.springframework.data.redis.connection.stream.*;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.stream.StreamListener;
//import org.springframework.data.redis.stream.StreamMessageListenerContainer;
//import org.springframework.data.redis.stream.Subscription;
//import org.springframework.stereotype.Service;
//import oridungjeol.duckhang.chat.common.RedisOperator;
//import oridungjeol.duckhang.chat.infrastructure.entity.ChatEntity;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.Map;
//
///**
// * redis consumer 예시
// * 해당 파일과 함께 RedisConfig, Redis Operator를 참고해 주세요
// */
//@Service
//public class RedisStreamsChatConsumer implements StreamListener<String, MapRecord<String, String, String>>,
//        InitializingBean,
//        DisposableBean
//{
//    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer;
//    private Subscription subscription;
//    private final RedisTemplate<String, String> redisTemplate;
//    private String streamKey;
//
//    private Logger log = LoggerFactory.getLogger(this.getClass().getName());
//
//    private final RedisOperator redisOperator;
//
//    public RedisStreamsChatConsumer(RedisTemplate<String, String> redisTemplate, RedisOperator redisOperator) {
//        this.redisTemplate = redisTemplate;
//        this.redisOperator = redisOperator;
//    }
//
//    /**
//     * 서버로부터 메시지를 받았을 때 실행되는 콜백
//     * @param message
//     */
//    @Override
//    public void onMessage(MapRecord<String, String, String> message) {
//        log.info("MessageId: " + message.getId());
//        log.info("Stream: " + message.getStream());
//        log.info("Body: " + message.getValue());
//
//        Map<String, String> rawData = message.getValue();
//
//        ChatEntity chat = new ChatEntity();
//        ChatEntity.builder()
//                .room_id(Integer.parseInt(rawData.get("room_id")))
//                .author_uuid(rawData.get("author_uuid"))
//                .content(rawData.get("content"))
//                .created_at(LocalDateTime.parse(rawData.get("created_at")))
//                .build();
//    }
//
//    /**
//     * Bean 소멸 시 실행되는 메서드
//     * @throws Exception
//     */
//    @Override
//    public void destroy() throws Exception {
//        if(this.subscription != null){
//            this.subscription.cancel();
//        }
//        if(this.listenerContainer != null){
//            this.listenerContainer .stop();
//        }
//    }
//
//    /**
//     * 빈 생성 시 실행되는 메서드
//     *
//     */
//    @Override
//    public void afterPropertiesSet() throws Exception {
//        redisTemplate.delete("chat-room:555");
//
//        this.streamKey = "chat-room:555";
//        this.listenerContainer = this.redisOperator.createStreamMessageListenerContainer();
//        this.subscription = this.listenerContainer.receive(
//                StreamOffset.fromStart("chat-room:555"),
//                this
//        );
//        this.subscription.await(Duration.ofSeconds(2));
//        this.listenerContainer.start();
//    }
//
//
//}
