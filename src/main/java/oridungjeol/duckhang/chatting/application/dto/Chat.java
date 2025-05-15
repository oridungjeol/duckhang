package oridungjeol.duckhang.chatting.application.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Chat {
//    private int id;
//    private String author_uuid;
//    private String content;
//    private LocalDateTime created_at;
//    private int room_id;
    private String type;
    private String content;
    private int room_id;
}
