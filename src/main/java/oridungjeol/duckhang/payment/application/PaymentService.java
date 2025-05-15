package oridungjeol.duckhang.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.rental.RentalRepository;

import oridungjeol.duckhang.payment.infrastructure.TossClient;
import oridungjeol.duckhang.payment.infrastructure.adapter.PaymentAdapter;
import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;
import oridungjeol.duckhang.payment.presentation.dto.PaymentRequestDto;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

//    private final SellRepository sellRepository;
//    private final PurchaseRepository purchaseRepository;
//    private final DelegateRepository delegateRepository;
    private final RentalRepository rentalRepository;

    private final TossClient tossClient;
    private final PaymentAdapter paymentAdapter;

    /**
     * 주문 ID 생성 및 PaymentEntity 저장
     */
    public String createOrderId(int boardId, String type) {
        int price = getPriceByTypeAndBoardId(type, boardId);
        String orderId = UUID.randomUUID().toString();

        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(orderId);
        payment.setBoardId(boardId);
        payment.setType(type.toUpperCase());
        payment.setAmount(price);
        payment.setStatus("PENDING");

        paymentAdapter.save(payment);

        log.info("주문 생성: boardId={}, type={}, price={}, orderId={}", boardId, type, price, orderId);
        return orderId;
    }

    /**
     * Toss 결제 승인 → PaymentEntity 상태 갱신
     */
    public JSONObject confirmPayment(PaymentRequestDto dto) throws Exception {
        // 1. Toss에 결제 승인 요청
        JSONObject tossResponse = tossClient.confirmPayment(dto);

        String status = (String) tossResponse.get("status");
        if (!"DONE".equalsIgnoreCase(status)) {
            throw new IllegalStateException("결제 승인 실패: status=" + status);
        }

        // 2. 기존 결제 정보 조회
        PaymentEntity payment = paymentAdapter.findByOrderId(dto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문입니다."));

        // 3. 금액 일치 여부 확인
        if (payment.getAmount() != dto.getAmount()) {
            throw new IllegalArgumentException("결제 금액이 주문 금액과 일치하지 않습니다.");
        }

        // 4. 결제 상태 및 승인 시간 업데이트
        payment.setPaymentKey(dto.getPaymentKey());
        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());

        paymentAdapter.save(payment);

        return tossResponse;
    }

    /**
     * type + boardId 조합으로 price 조회
     */
    private int getPriceByTypeAndBoardId(String type, int boardId) {
        return switch (type.toUpperCase()) {
//            case "SELL" -> sellRepository.findById(boardId)
//                    .orElseThrow(() -> new IllegalArgumentException("SELL 게시글이 없습니다."))
//                    .getPrice();
//            case "PURCHASE" -> purchaseRepository.findById(boardId)
//                    .orElseThrow(() -> new IllegalArgumentException("PURCHASE 게시글이 없습니다."))
//                    .getPrice();
//            case "DELEGATE" -> delegateRepository.findById(boardId)
//                    .orElseThrow(() -> new IllegalArgumentException("DELEGATE 게시글이 없습니다."))
//                    .getPrice();
            case "RENTAL" -> rentalRepository.findById(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("RENTAL 게시글이 없습니다."))
                    .getPrice();
            default -> throw new IllegalArgumentException("지원하지 않는 결제 타입입니다.");
        };
    }
}
