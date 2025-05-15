package oridungjeol.duckhang.payment.presentation;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.payment.application.PaymentService;
import oridungjeol.duckhang.payment.presentation.dto.PaymentRequestDto;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<JSONObject> confirm(@RequestBody PaymentRequestDto dto) throws Exception {
        JSONObject result = paymentService.confirmPayment(dto);
        return ResponseEntity.ok(result);
    }
}