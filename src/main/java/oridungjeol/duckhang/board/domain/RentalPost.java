package oridungjeol.duckhang.board.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentalPost {
    private long boardId;

    private int price;

    private int deposit;

    public RentalPost(Long boardId, int price, int deposit) {
        if (price <= 0) throw new IllegalArgumentException("가격은 0보다 커야 합니다.");
        if (deposit < 0) throw new IllegalArgumentException("보증금은 음수가 될 수 없습니다.");
        this.boardId = boardId;
        this.price = price;
        this.deposit = deposit;
    }

    public void updatePriceAndDeposit(Integer price, Integer deposit) {

        if (price != null && price > 0) {
            this.price = price;
        }

        if (deposit != null && deposit > 0) {
            this.deposit = deposit;
        }
    }
}
