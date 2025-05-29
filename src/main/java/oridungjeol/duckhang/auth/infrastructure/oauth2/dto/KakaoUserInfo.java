package oridungjeol.duckhang.auth.infrastructure.oauth2.dto;

public record KakaoUserInfo(
        Long id,
        KakaoAccount kakao_account
) {
    public record KakaoAccount(
            KakaoProfile profile,
            String email
    ) {
        public record KakaoProfile(
                String nickname,
                String profile_image_url
        ) {
        }
    }
}