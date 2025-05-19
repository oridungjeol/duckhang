package oridungjeol.duckhang.board.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
public class PurchaseRequestDto {
    private String title;
    private String content;
    private String imageUrl;
    private int price;
}
