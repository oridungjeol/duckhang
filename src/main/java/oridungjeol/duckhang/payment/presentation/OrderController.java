package oridungjeol.duckhang.payment.presentation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import oridungjeol.duckhang.payment.application.PaymentService;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class OrderController {

    private final PaymentService paymentService;

    @PostMapping("/create-order-id")
    public ResponseEntity<JSONObject> createOrderId(@RequestBody Map<String, Object> request) {
        if (!request.containsKey("boardId") || !request.containsKey("type")) {
            JSONObject error = new JSONObject();
            error.put("error", "boardId 또는 type이 누락되었습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        try {
            int boardId = Integer.parseInt(request.get("boardId").toString());
            String type = request.get("type").toString();

            String orderId = paymentService.createOrderId(boardId, type);

            JSONObject obj = new JSONObject();
            obj.put("orderId", orderId);

            return ResponseEntity.ok(obj);

        } catch (NumberFormatException e) {
            JSONObject error = new JSONObject();
            error.put("error", "boardId는 숫자여야 합니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            JSONObject error = new JSONObject();
            error.put("error", "서버 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

}
