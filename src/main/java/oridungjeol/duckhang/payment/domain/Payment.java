package oridungjeol.duckhang.payment.domain;

import lombok.Getter;

@Getter
public class Payment {

    private String orderId;
    private String paymentKey;
    private String amount;

    public Payment(String orderId, String paymentKey, String amount) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
    }
}
