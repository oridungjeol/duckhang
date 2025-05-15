package oridungjeol.duckhang.security.oauth2.dto;

public record KakaoAccount(
        KakaoProfile profile,
        String email
) {}
