package oridungjeol.duckhang.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.auth.infrastructure.repository.AuthJpaRepository;
import oridungjeol.duckhang.user.domain.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;
import oridungjeol.duckhang.user.presentation.dto.ProfileResponse;
import oridungjeol.duckhang.user.support.UserConverter;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userJpaRepository;
    private final AuthJpaRepository authJpaRepository;

    public ProfileResponse getProfile(UUID uuid) {
        User user = UserConverter.toDomain(userJpaRepository.findByUuid(uuid).get());
        return ProfileResponse.builder()
                .uuid(uuid.toString())
                .nickname(user.getNickname())
                .scope(user.getScope())
                .build();
    }

    /**
     * 주어진 UUID를 가진 사용자의 계정 정보를 영구적으로 삭제합니다.
     *
     * <ul>
     *   <li>사용자의 프로필 정보 (UserJpaRepository)</li>
     *   <li>사용자의 인증 정보 (AuthJpaRepository)</li>
     * </ul>
     *
     * <p>삭제된 데이터는 복구할 수 없으며, 해당 UUID로는 더 이상 로그인할 수 없습니다.
     * 트랜잭션으로 처리되어 모든 삭제 작업이 함께 성공하거나 실패합니다.
     *
     * @param uuid 삭제할 사용자의 UUID
     */
    @Transactional
    public void deleteUser(UUID uuid) {
        userJpaRepository.deleteUserByUuid(uuid);
        authJpaRepository.deleteAuthByUuid(uuid);
    }
}
