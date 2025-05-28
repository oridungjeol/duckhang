package oridungjeol.duckhang.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SellPost implements PricedBoardDetail{
    private Long boardId;
    private int price;

    public SellPost(Long boardId, Integer price) {
        this.boardId = boardId;
        if (price <= 0) throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        this.price = price;
    }

    public void updatePrice(Integer price) {
        if (price != null && price > 0) {
            this.price = price;
        }
    }
}
