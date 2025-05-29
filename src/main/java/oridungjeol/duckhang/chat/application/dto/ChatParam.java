package oridungjeol.duckhang.chat.application.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ChatParam {
    private final String author_uuid;
    private final String name;
    private final long board_id;
    private final String type;
}
