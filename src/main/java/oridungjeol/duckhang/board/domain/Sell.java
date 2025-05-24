package oridungjeol.duckhang.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Sell {
    private Long id;
    private Long boardId;
    private int price;
    
    public void update(int price) {
        this.price = price;
    }
}
