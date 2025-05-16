package oridungjeol.duckhang.user.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.auth.infrastructure.repository.AuthJpaRepository;
import oridungjeol.duckhang.security.oauth2.KakaoApiClient;
import oridungjeol.duckhang.user.application.service.UserService;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;
import oridungjeol.duckhang.user.presentation.dto.ProfileResponse;
import oridungjeol.duckhang.user.presentation.dto.UpdatePrivacyRequest;
import oridungjeol.duckhang.user.presentation.dto.UpdateProfileRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserUseCase {

    private final UserService userService;
    private final KakaoApiClient kakaoApiClient;
    private final AuthJpaRepository authJpaRepository;
    private final UserJpaRepository userJpaRepository;

    public ProfileResponse getProfile(String uuid) {
        return userService.getProfile(UUID.fromString(uuid));
    }

    /**
     * 프로필 정보를 수정합니다.
     *
     * <p>
     * 요청한 프로필을 가진 사람의 프로필을 수정합니다.
     *
     * @param userId               수정할 사용자의 UUID 문자열
     * @param updateProfileRequest 수정할 프로필 정보의 dto
     */
    public void updateProfile(String userId, UpdateProfileRequest updateProfileRequest) {
        UUID uuid = UUID.fromString(userId);
        userService.updateProfile(uuid, updateProfileRequest);
    }

    /**
     * 개인정보를 수정합니다.
     *
     * <p>
     * 요청한 사용자의 개인정보를 수정합니다.
     *
     * @param userId 수정할 사용자의 UUID 문자열
     * @param updatePrivacyRequest 수정된 개인정보 내용
     */
    public void updatePrivacy(String userId, UpdatePrivacyRequest updatePrivacyRequest) {
        UUID uuid = UUID.fromString(userId);
        userService.updatePrivacy(uuid, updatePrivacyRequest);
    }

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
