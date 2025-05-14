package oridungjeol.duckhang.payment.presentation;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.payment.application.PaymentService;

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

    @PostMapping(value = "/confirm")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {
        JSONObject response = paymentService.confirmPayment(jsonBody);
        return ResponseEntity.status(200).body(response);
    }
}
