package oridungjeol.duckhang.board.search.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Builder;
import lombok.Getter;
import oridungjeol.duckhang.board.support.enums.BoardType;

import java.time.LocalDateTime;

@Builder
@Getter
public class BoardSearchResultDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Integer price;
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
//    private LocalDateTime createdAt;
    private BoardType boardType;
}
