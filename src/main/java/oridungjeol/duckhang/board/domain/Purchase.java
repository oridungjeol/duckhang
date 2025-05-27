package oridungjeol.duckhang.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Purchase implements PricedBoardDetail{
    private Long boardId;
    private int price;
    
    public void updatePrice(int price) {
        this.price = price;
    }
}
