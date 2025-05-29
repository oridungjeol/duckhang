package oridungjeol.duckhang.payment.presentation.dto;

import lombok.Getter;

@Getter
public class PaymentRequestDto {
    private String orderId;
    private String paymentKey;
    private int amount;
    private int boardId;
    private String type;
}
