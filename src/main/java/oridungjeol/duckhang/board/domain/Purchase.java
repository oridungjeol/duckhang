package oridungjeol.duckhang.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Purchase {
    private Long id;
    private Long boardId;
    private int price;
    
    public void update(int price) {
        this.price = price;
    }
}
