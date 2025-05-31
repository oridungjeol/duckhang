package oridungjeol.duckhang.board.search.domain;

import lombok.Builder;
import lombok.Getter;
import oridungjeol.duckhang.board.domain.BoardType;

import java.time.LocalDateTime;

@Builder
@Getter public class SearchBoardResultDto {
    private Long id;
    private String title;
    private String content;
    private String imageUrl;
    private Integer price;
    private LocalDateTime createdAt;
    private BoardType boardType;
}
