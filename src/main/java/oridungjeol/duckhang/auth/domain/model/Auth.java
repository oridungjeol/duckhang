package oridungjeol.duckhang.auth.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class Auth {
    private final UUID uuid;
    private final String provider;
    private final String providerId;
    private final String accessToken;
    private final String refreshToken;
}
