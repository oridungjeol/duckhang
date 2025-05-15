package oridungjeol.duckhang.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.RentalEntity;
import oridungjeol.duckhang.payment.domain.Payment;
import oridungjeol.duckhang.payment.infrastructure.TossClient;
import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;
import oridungjeol.duckhang.payment.presentation.dto.PaymentRequestDto;
import oridungjeol.duckhang.payment.support.OrderIdGenerator;
import oridungjeol.duckhang.payment.support.PaymentMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TossClient tossClient;
    private final PaymentRepository paymentRepository;

    // 주문 ID 생성
    public String createOrderId() {
        return OrderIdGenerator.generateOrderId();
    }

    // 결제 확인 후 저장
    public JSONObject confirmPayment(PaymentRequestDto dto) throws Exception {
        JSONObject tossResponse = tossClient.confirmPayment(dto);

        String status = (String) tossResponse.get("status");
        if ("DONE".equalsIgnoreCase(status)) {
            Payment payment = new Payment(
                    dto.getOrderId(),
                    dto.getPaymentKey(),
                    dto.getAmount()
            );
            PaymentEntity paymentEntity = PaymentMapper.toEntity(payment);
            paymentRepository.save(paymentEntity);
        }

        return tossResponse;
    }

}
