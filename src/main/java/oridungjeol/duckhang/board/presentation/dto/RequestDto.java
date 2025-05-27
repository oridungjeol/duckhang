package oridungjeol.duckhang.board.presentation.dto;

import lombok.Getter;
import oridungjeol.duckhang.board.domain.BoardType;

@Getter
public class RequestDto {
    private String title;
    private String content;
    private String imageUrl;
    private int price;
}
