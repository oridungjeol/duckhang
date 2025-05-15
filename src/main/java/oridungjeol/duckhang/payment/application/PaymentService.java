package oridungjeol.duckhang.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.RentalEntity;
import oridungjeol.duckhang.board.RentalRepository;
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
    private final RentalRepository rentalRepository;

    // 주문 ID 생성 (boardId 기반 가격 포함)
    public String createOrderId(int boardId) {
        RentalEntity rental = rentalRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 boardId의 렌탈 정보가 없습니다."));

        int price = rental.getPrice();
        String uuid = java.util.UUID.randomUUID().toString();

        return String.format("board-%d-%d-%s", boardId, price, uuid);
    }

    // 결제 확인 후 저장
    public JSONObject confirmPayment(PaymentRequestDto dto) throws Exception {
        String[] parts = dto.getOrderId().split("-");
        if (parts.length < 4 || !parts[0].equals("board")) {
            throw new IllegalArgumentException("잘못된 orderId 형식입니다.");
        }

        int expectedPrice = Integer.parseInt(parts[2]);
        if (dto.getAmount() != expectedPrice) {
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }

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
            log.info("Payment saved for orderId: {}", dto.getOrderId());
        } else {
            log.warn("Payment status is not DONE: {}", status);
        }

        return tossResponse;
    }
}