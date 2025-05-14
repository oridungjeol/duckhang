package oridungjeol.duckhang.payment.presentation;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.payment.application.PaymentService;
import oridungjeol.duckhang.payment.presentation.dto.PaymentRequestDto;

@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class WidgetController {

    private final PaymentService paymentService;

    public WidgetController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping(value = "/create-order-id")
    public ResponseEntity<JSONObject> createOrderId() {
        String orderId = paymentService.createOrderId();
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);

        log.info("Generated orderId: {}", orderId);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/confirm")
    public ResponseEntity<JSONObject> confirm(@RequestBody PaymentRequestDto dto) throws Exception {
        JSONObject result = paymentService.confirmPayment(dto);
        return ResponseEntity.ok(result);
    }
}
