package oridungjeol.duckhang.payment.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.payment.application.PaymentService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final PaymentService paymentService;

    @PostMapping("/create-order-id")
    public ResponseEntity<JSONObject> createOrderId(@RequestBody Map<String, Object> request) {
        int boardId = Integer.parseInt(request.get("boardId").toString());
        String orderId = paymentService.createOrderId(boardId);

        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);

        log.info(orderId);

        return ResponseEntity.ok(obj);
    }
}