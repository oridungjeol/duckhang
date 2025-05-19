package oridungjeol.duckhang.auth.infrastructure.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoUnlinkService {

    private final KakaoApiClient kakaoApiClient;

    public void processUnlink(String accessToken) {
        kakaoApiClient.unlinkUserWithAccessToken(accessToken);
    }

    public void processUnlinkWithAdmin(String kakaoUserId, String adminKey) {
        kakaoApiClient.unlinkUserWithAdminKey(kakaoUserId, adminKey);
    }
}