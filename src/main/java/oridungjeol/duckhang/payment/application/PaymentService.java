package oridungjeol.duckhang.payment.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.board.rental.RentalRepository;
import oridungjeol.duckhang.board.sell.SellRepository;
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

    private final SellRepository sellRepository;
    private final RentalRepository rentalRepository;

    private final TossClient tossClient;
    private final PaymentAdapter paymentAdapter;

    // 주문 생성 - boardId(게시글 board_id), type 받아 처리
    public String createOrderId(int boardId, String type) {
        int price = getPriceByTypeAndBoardId(type, boardId);

        String orderId = OrderIdGenerator.generateOrderId();

        PaymentEntity payment = new PaymentEntity();
        payment.setOrderId(orderId);
        payment.setBoardId(boardId); // 게시글 boardId
        payment.setType(type.toUpperCase());
        payment.setAmount(price);
        payment.setStatus("PENDING");

        paymentAdapter.save(payment);

        log.info("주문 생성: orderId={}, type={},  boardId={}, price={}",
                orderId, type, boardId, price);

        return orderId;
    }

    // 결제 확인
    public JSONObject confirmPayment(PaymentRequestDto dto) throws Exception {
        JSONObject tossResponse = tossClient.confirmPayment(dto);

        if (!"DONE".equalsIgnoreCase((String) tossResponse.get("status"))) {
            throw new IllegalStateException("결제 승인 실패");
        }

        PaymentEntity payment = paymentAdapter.findByOrderId(dto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

        if (payment.getAmount() != dto.getAmount()) {
            throw new IllegalArgumentException("금액 불일치");
        }

        payment.setPaymentKey(dto.getPaymentKey());
        payment.setStatus("PAID");
        payment.setPaidAt(LocalDateTime.now());

        paymentAdapter.save(payment);

        return tossResponse;
    }

    // 타입과 board_id를 기준으로 가격 조회
    private int getPriceByTypeAndBoardId(String type, Integer BoardId) {
        switch (type.toUpperCase()) {
            case "SELL" -> {
                var sell = sellRepository.findByBoardId(BoardId)
                        .orElseThrow(() -> new IllegalArgumentException("[판매] 게시글이 없습니다."));
                return sell.getPrice();
            }
            case "RENTAL" -> {
                var rental = rentalRepository.findByBoardId(BoardId)
                        .orElseThrow(() -> new IllegalArgumentException("[대여] 게시글이 없습니다."));
                return rental.getPrice() + rental.getDeposit();
            }
            default -> throw new IllegalArgumentException("지원하지 않는 결제 타입입니다.");
        }
    }
}
