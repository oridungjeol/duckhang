package oridungjeol.duckhang.auth.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.auth.infrastructure.jwt.JwtGenerator;
import oridungjeol.duckhang.auth.infrastructure.jwt.JwtParser;
import oridungjeol.duckhang.auth.presentation.dto.AccessTokenRequestDto;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthUseCase {

    private final UserJpaRepository userJpaRepository;
    private final JwtParser jwtParser;
    private final JwtGenerator jwtGenerator;

    public String getAccessToken(AccessTokenRequestDto accessTokenRequestDto) {
        if (jwtParser.validateToken(accessTokenRequestDto.getRefreshToken())) {
            UUID uuid = UUID.fromString(accessTokenRequestDto.getUuid());
            String nickname = userJpaRepository.findByUuid(uuid).get().getNickname();
            CustomPrincipal principal = new CustomPrincipal(uuid, nickname);
            return jwtGenerator.createAccessToken(principal);
        }
        return null;
    }
}
