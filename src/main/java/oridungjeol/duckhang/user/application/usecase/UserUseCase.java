package oridungjeol.duckhang.user.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.auth.infrastructure.repository.AuthJpaRepository;
import oridungjeol.duckhang.security.oauth2.KakaoApiClient;
import oridungjeol.duckhang.user.application.service.UserService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserService userService;
    private final KakaoApiClient kakaoApiClient;
    private final AuthJpaRepository authJpaRepository;

    /**
     * 주어진 사용자 ID에 해당하는 사용자를 탈퇴처리합니다.
     *
     * <p>
     * 카카오 플랫폼과 서버의 연결을 해제합니다.
     * <p>
     * 데이터베이스에 있는 사용자 정보를 모두 삭제합니다.
     * <p>
     * 트랜잭션으로 처리되어 모든 삭제 작업이 함께 성공하거나 실패합니다.
     *
     * @param userId 삭제할 사용자의 UUID 문자열
     */
    @Transactional
    public void deleteUser(String userId) {
        UUID uuid = UUID.fromString(userId);
        kakaoApiClient.unlinkUserWithAccessToken(authJpaRepository.findByUuid(uuid).get().getProviderId());
        userService.deleteUser(UUID.fromString(userId));
    }
}
