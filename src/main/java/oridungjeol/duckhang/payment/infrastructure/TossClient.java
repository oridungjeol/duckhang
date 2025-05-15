package oridungjeol.duckhang.payment.infrastructure;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.payment.common.PaymentConfig;
import oridungjeol.duckhang.payment.presentation.dto.PaymentRequestDto;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class TossClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PaymentConfig paymentConfig;

    public TossClient(PaymentConfig paymentConfig) {
        this.paymentConfig = paymentConfig;
    }

    // 결제 확인 요청
    public JSONObject confirmPayment(PaymentRequestDto paymentDto) throws IOException, ParseException {
        JSONObject obj = new JSONObject();
        obj.put("orderId", paymentDto.getOrderId());
        obj.put("amount", paymentDto.getAmount());
        obj.put("paymentKey", paymentDto.getPaymentKey());

        String authorizationHeader = createAuthorizationHeader(paymentConfig.getSecretKey());
        return sendPaymentConfirmationRequest(obj, authorizationHeader);
    }

    // Authorization 헤더 생성
    private String createAuthorizationHeader(String secretKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedBytes);
    }

    // 결제 확인 요청을 Toss API로 전송
    private JSONObject sendPaymentConfirmationRequest(JSONObject requestData, String authorizationHeader) throws IOException, ParseException {
        String urlString = paymentConfig.getBaseUrl() + paymentConfig.getConfirmEndpoint();
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("Authorization", authorizationHeader);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        // 요청 데이터 전송
        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestData.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }

        // 응답 처리
        int code = connection.getResponseCode();
        InputStream responseStream = code == 200 ? connection.getInputStream() : connection.getErrorStream();
        try (Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) new org.json.simple.parser.JSONParser().parse(reader);
        }
    }
}
