package oridungjeol.duckhang.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Payment {

    private final String orderId;     // 주문 고유 ID
    private final String paymentKey;  // 결제 키
    private final int amount;         // 결제 금액
    private final Long boardId;        // 게시글의 board_id (외래키)
    private final String type;        // 게시글 타입 (SELL, RENTAL 등)
}
