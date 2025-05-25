package oridungjeol.duckhang.auth.presentation.dto;

import lombok.Getter;

@Getter
public class AccessTokenRequestDto {
    private String refreshToken;
    private String uuid;
}
