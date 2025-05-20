package oridungjeol.duckhang.board.application.dto;

import lombok.Getter;

@Getter
public class RequestDto {
    private String title;
    private String content;
    private String imageUrl;
    private int price;
}
