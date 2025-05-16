package oridungjeol.duckhang.payment.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class PaymentConfig {

    @Value("${payment.secret-key}")
    private String secretKey;

    @Value("${payment.base-url}")
    private String baseUrl;

    @Value("${payment.confirm-endpoint}")
    private String confirmEndpoint;

    @Value("${payment.cancel-endpoint}")
    private String cancelEndpoint;

}
