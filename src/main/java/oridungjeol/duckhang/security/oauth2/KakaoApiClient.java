package oridungjeol.duckhang.security.oauth2;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoApiClient {

    private final RestTemplate restTemplate;

    public KakaoApiClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    private static final String UNLINK_URL = "https://kapi.kakao.com/v1/user/unlink";

    public void unlinkUserWithAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<Void> request = new HttpEntity<>(headers);
        restTemplate.exchange(UNLINK_URL, HttpMethod.POST, request, String.class);
    }

    public void unlinkUserWithAdminKey(String kakaoUserId, String adminKey) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + adminKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String body = "target_id_type=user_id&target_id=" + kakaoUserId;
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        restTemplate.exchange(UNLINK_URL, HttpMethod.POST, request, String.class);
    }
}
