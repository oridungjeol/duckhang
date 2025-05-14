package oridungjeol.duckhang.payment.domain;

public class Payment {

    private String orderId;
    private String paymentKey;
    private String amount;

    public Payment(String orderId, String paymentKey, String amount) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public String getAmount() {
        return amount;
    }

    // 주문 ID 생성
    public static String generateOrderId() {
        return java.util.UUID.randomUUID().toString();
    }
}
