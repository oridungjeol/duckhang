package oridungjeol.duckhang.board.search.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import oridungjeol.duckhang.board.support.enums.BoardType;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Builder
@Getter
public class BoardSearchResultDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Integer price;
    private LocalDateTime createdAt;
    private BoardType boardType;
}
