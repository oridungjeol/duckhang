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

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<JSONObject> cancelDeposit(@PathVariable String orderId) throws Exception {
        JSONObject result = paymentService.cancelRentalDeposit(orderId);

        // 상태가 CANCELED가 아닌 경우 예외 처리
        if (!"PARTIAL_CANCELED".equalsIgnoreCase((String) result.get("status"))) {
            JSONObject error = new JSONObject();
            error.put("error", "환불 요청이 실패했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        // 프론트에서 사용할 정보 포함
        JSONObject response = new JSONObject();
        response.put("orderId", result.get("orderId"));
        response.put("refundId", result.get("paymentKey")); // Toss에서는 paymentKey가 환불 키로 사용됨
        response.put("cancelAmount", result.get("cancelAmount"));
        response.put("refundedAt", result.get("canceledAt")); // 환불 일시
        response.put("status", "canceled");

        log.info("환불 응답 전체: {}", result.toJSONString());

        return ResponseEntity.ok(response);
    }

}
