package oridungjeol.duckhang.payment.domain;

import lombok.Data;

@Data
public class Payment {
    private String orderId;
    private String paymentKey;
    private int amount;

    public Payment(String orderId, String paymentKey, int amount) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
    }
}
