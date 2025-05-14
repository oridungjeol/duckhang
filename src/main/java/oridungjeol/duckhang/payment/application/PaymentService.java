package oridungjeol.duckhang.payment.application;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.payment.domain.Payment;
import oridungjeol.duckhang.payment.infrastructure.TossClient;


@Slf4j
@Service
public class PaymentService {

    private final TossClient tossClient;

    public PaymentService(TossClient tossClient) {
        this.tossClient = tossClient;
    }

    // 주문 ID 생성
    public String createOrderId() {
        return Payment.generateOrderId();
    }

    // 결제 확인
    public JSONObject confirmPayment(String jsonBody) throws Exception {
        JSONObject requestData = parseRequestBody(jsonBody);

        String orderId = (String) requestData.get("orderId");
        String paymentKey = (String) requestData.get("paymentKey");
        String amount = (String) requestData.get("amount");

        Payment payment = new Payment(orderId, paymentKey, amount);

        // Toss API 호출
        return tossClient.confirmPayment(payment);
    }

    private JSONObject parseRequestBody(String jsonBody) throws ParseException {
        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(jsonBody);
    }
}
