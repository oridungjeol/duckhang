package oridungjeol.duckhang.payment.domain;

import lombok.Data;
import lombok.Getter;

@Data
public class Payment {

    private final String orderId;
    private final String paymentKey;
    private final int amount;
    private final int boardId;
    private final String type;

    public Payment(String orderId, String paymentKey, int amount, int boardId, String type) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.boardId = boardId;
        this.type = type;
    }
}
