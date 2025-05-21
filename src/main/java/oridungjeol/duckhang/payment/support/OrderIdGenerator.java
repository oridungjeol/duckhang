package oridungjeol.duckhang.payment.support;

public class OrderIdGenerator {
    public static String generateOrderId() {
        String timestamp = new java.text.SimpleDateFormat("yyyyMMddHHmmssSSS").format(new java.util.Date());
        int random = (int)(Math.random() * 10000);
        return timestamp + String.format("%04d", random);
    }
}
