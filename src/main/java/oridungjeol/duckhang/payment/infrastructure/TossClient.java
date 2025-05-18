package oridungjeol.duckhang.payment.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
import java.util.UUID;

@Slf4j
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
        return sendPostRequest(paymentConfig.getConfirmEndpoint(), obj, authorizationHeader);
    }

    // 결제 취소 요청 (보증금 환불)
    public JSONObject cancelPayment(String paymentKey, int cancelAmount) throws IOException, ParseException {
        JSONObject body = new JSONObject();
        body.put("cancelReason", "고객 요청");
        body.put("cancelAmount", cancelAmount);

        String endpoint = "/" + paymentKey + "/cancel";
        String authorizationHeader = createAuthorizationHeader(paymentConfig.getSecretKey());
        String idempotencyKey = UUID.randomUUID().toString();

        return sendPostRequest(endpoint, body, authorizationHeader, idempotencyKey);
    }

    // 결제 정보 조회(GET)
    public JSONObject getPaymentInfo(String paymentKey) throws IOException, ParseException {
        String urlString = paymentConfig.getBaseUrl() + "/" + paymentKey;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        String authorizationHeader = createAuthorizationHeader(paymentConfig.getSecretKey());
        connection.setRequestProperty("Authorization", authorizationHeader);
        connection.setRequestProperty("Content-Type", "application/json");

        connection.setRequestMethod("GET");
        connection.setDoOutput(false);

        int code = connection.getResponseCode();
        InputStream responseStream = code == 200 ? connection.getInputStream() : connection.getErrorStream();

        try (Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        }
    }

    // Authorization 헤더 생성
    private String createAuthorizationHeader(String secretKey) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        return "Basic " + new String(encodedBytes);
    }

    // POST 요청 전송 (공통)
    private JSONObject sendPostRequest(String endpoint, JSONObject requestBody, String authorizationHeader) throws IOException, ParseException {
        return sendPostRequest(endpoint, requestBody, authorizationHeader, null);
    }

    private JSONObject sendPostRequest(String endpoint, JSONObject requestBody, String authorizationHeader, String idempotencyKey) throws IOException, ParseException {
        String urlString = paymentConfig.getBaseUrl() + endpoint;
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("Authorization", authorizationHeader);
        connection.setRequestProperty("Content-Type", "application/json");
        if (idempotencyKey != null) {
            connection.setRequestProperty("Idempotency-Key", idempotencyKey);
        }

        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        try (OutputStream outputStream = connection.getOutputStream()) {
            outputStream.write(requestBody.toString().getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        }

        int code = connection.getResponseCode();
        InputStream responseStream = code == 200 ? connection.getInputStream() : connection.getErrorStream();
        try (Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8)) {
            return (JSONObject) new JSONParser().parse(reader);
        }
    }
}
