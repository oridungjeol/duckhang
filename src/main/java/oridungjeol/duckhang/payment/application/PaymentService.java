package oridungjeol.duckhang.payment.application;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.payment.infrastructure.TossClient;
import oridungjeol.duckhang.payment.presentation.dto.PaymentRequestDto;
import oridungjeol.duckhang.payment.support.OrderIdGenerator;

@Slf4j
@Service
public class PaymentService {

    private final TossClient tossClient;

    public PaymentService(TossClient tossClient) {
        this.tossClient = tossClient;
    }

    // 주문 ID 생성
    public String createOrderId() {
        return OrderIdGenerator.generateOrderId();
    }

    // 결제 확인
    public JSONObject confirmPayment(PaymentRequestDto dto) throws Exception {
        return tossClient.confirmPayment(dto);
    }
}
