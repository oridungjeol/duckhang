package oridungjeol.duckhang.payment.presentation;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.payment.application.PaymentService;
import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;
import oridungjeol.duckhang.payment.presentation.dto.PaymentRequestDto;


@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor

public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<JSONObject> confirm(@RequestBody PaymentRequestDto dto) throws Exception {
        JSONObject result = paymentService.confirmPayment(dto);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<PaymentEntity> getPaymentByBoardId(@PathVariable Long boardId) {
        PaymentEntity payment = paymentService.getPaymentByBoardId(boardId);
        return ResponseEntity.ok(payment);
    }
}