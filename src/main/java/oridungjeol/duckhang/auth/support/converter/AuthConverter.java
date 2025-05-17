package oridungjeol.duckhang.auth.support.converter;

import oridungjeol.duckhang.auth.domain.model.Auth;

public class AuthConverter {

    public static oridungjeol.duckhang.auth.infrastructure.entity.Auth toEntity(Auth auth) {
        return oridungjeol.duckhang.auth.infrastructure.entity.Auth.builder()
                .uuid(auth.getUuid())
                .provider(auth.getProvider())
                .providerId(auth.getProviderId())
                .refreshToken(auth.getRefreshToken())
                .build();
    }

    public static Auth toDomain(oridungjeol.duckhang.auth.infrastructure.entity.Auth auth) {
        return Auth.builder()
                .uuid(auth.getUuid())
                .provider(auth.getProvider())
                .providerId(auth.getProviderId())
                .refreshToken(auth.getRefreshToken())
                .build();
    }
}
