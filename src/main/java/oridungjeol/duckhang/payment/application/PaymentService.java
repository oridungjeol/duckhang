package oridungjeol.duckhang.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.infrastructure.entity.RentalEntity;
import oridungjeol.duckhang.board.infrastructure.repository.PurchaseJpaRepository;
import oridungjeol.duckhang.board.infrastructure.repository.RentalJpaRepository;
import oridungjeol.duckhang.board.infrastructure.repository.SellJpaRepository;
import oridungjeol.duckhang.payment.infrastructure.TossClient;
import oridungjeol.duckhang.payment.infrastructure.adapter.PaymentAdapter;
import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;
import oridungjeol.duckhang.payment.presentation.dto.PaymentRequestDto;
import oridungjeol.duckhang.payment.support.OrderIdGenerator;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final SellJpaRepository sellJpaRepository;
    private final RentalJpaRepository rentalJpaRepository;
    private final PurchaseJpaRepository purchaseJpaRepository;
    private final RentalJpaRepository delegateJpaRepository;
    private final TossClient tossClient;
    private final PaymentAdapter paymentAdapter;

    /**
     * 주문 생성
     */
    public String createOrderId(Long boardId, String type) {
        int price = getPriceByTypeAndBoardId(type, boardId);
        String orderId = OrderIdGenerator.generateOrderId();

        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(orderId);
        payment.setBoardId(boardId);
        payment.setType(type.toUpperCase());
        payment.setAmount(price);
        payment.setStatus("PENDING");

        paymentAdapter.save(payment);

        log.info("주문 생성: orderId={}, type={}, boardId={}, price={}",
                orderId, type, boardId, price);

        return orderId;
    }

    /**
     * 결제 승인 확인
     */
    public JSONObject confirmPayment(PaymentRequestDto dto) throws Exception {
        JSONObject tossResponse = tossClient.confirmPayment(dto);

        if (!"DONE".equalsIgnoreCase((String) tossResponse.get("status"))) {
            throw new IllegalStateException("결제 승인 실패");
        }

        PaymentEntity payment = paymentAdapter.findByOrderId(dto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

        if (payment.getAmount() != dto.getAmount()) {
            throw new IllegalArgumentException("결제 금액 불일치");
        }

        payment.setPaymentKey(dto.getPaymentKey());
        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());

        paymentAdapter.save(payment);
        return tossResponse;
    }

    /**
     * 보증금 부분 취소 (RENTAL only)
     */
    public JSONObject cancelRentalDeposit(String orderId) throws Exception {
        PaymentEntity payment = paymentAdapter.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보가 존재하지 않습니다."));


        // 이미 부분 환불 됐으면 예외 발생
        if (payment.isPartiallyRefunded()) {
            throw new IllegalStateException("이미 부분 환불이 완료된 결제입니다. 중복 환불은 불가능합니다.");
        }

        RentalEntity rental = rentalJpaRepository.findByBoardId(payment.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("해당 [대여] 게시글이 없습니다."));

        int deposit = rental.getDeposit();

        // Toss 결제 정보 조회
        JSONObject paymentInfo = tossClient.getPaymentInfo(payment.getPaymentKey());

        Long balanceAmountLong = (Long) paymentInfo.get("balanceAmount");
        int refundableAmount = balanceAmountLong != null ? balanceAmountLong.intValue() : 0;

        if (deposit > refundableAmount) {
            throw new IllegalStateException("환불 가능 금액 초과: 요청 금액=" + deposit + ", 환불 가능 금액=" + refundableAmount);
        }

        // 환불 요청
        JSONObject result = tossClient.cancelPayment(payment.getPaymentKey(), deposit);
        log.warn("Toss 환불 응답: {}", result.toJSONString());

        if (!"PARTIAL_CANCELED".equalsIgnoreCase((String) result.get("status"))) {
            throw new IllegalStateException("환불 요청이 실패했습니다.");
        }

        // 부분 환불 완료 표시
        payment.setPartiallyRefunded(true);
        payment.setStatus("CANCELED");
        payment.setRefundedAt(LocalDateTime.now());
        paymentAdapter.save(payment);

        // 응답 구성
        result.put("cancelAmount", deposit);
        result.put("refundedAt", payment.getRefundedAt());
        result.put("refundId", payment.getOrderId() + "-refund");

        return result;
    }

    /**
     * 게시글 타입 + boardId로 결제 금액 조회
     */
    private int getPriceByTypeAndBoardId(String type, Long boardId) {
        return switch (type.toUpperCase()) {
            case "SELL" -> sellJpaRepository.findByBoardId(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("[판매] 게시글이 존재하지 않습니다."))
                    .getPrice();
            case "PURCHASE" -> purchaseJpaRepository.findByBoardId(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("[구매] 게시글이 존재하지 않습니다."))
                    .getPrice();
            case "DELEGATE" -> delegateJpaRepository.findByBoardId(boardId)
                    .orElseThrow(() -> new IllegalArgumentException("[대리] 게시글이 존재하지 않습니다."))
                    .getPrice();
            case "RENTAL" -> {
                RentalEntity rental = rentalJpaRepository.findByBoardId(boardId)
                        .orElseThrow(() -> new IllegalArgumentException("[대여] 게시글이 존재하지 않습니다."));
                yield rental.getPrice() + rental.getDeposit(); // 가격 + 보증금
            }
            default -> throw new IllegalArgumentException("지원하지 않는 결제 타입입니다.");
        };
    }
}
