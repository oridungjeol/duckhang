package oridungjeol.duckhang.user.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import oridungjeol.duckhang.auth.infrastructure.repository.AuthJpaRepository;
import oridungjeol.duckhang.user.domain.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;
import oridungjeol.duckhang.user.presentation.dto.ProfileResponse;
import oridungjeol.duckhang.user.presentation.dto.UpdatePrivacyRequest;
import oridungjeol.duckhang.user.presentation.dto.UpdateProfileRequest;
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
     * 사용자의 프로필 정보를 수정합니다.
     *
     * <p>주어진 UUID를 통해 기존 사용자를 조회한 후, 요청 값이 존재하는 필드만 기존 정보와 병합하여 새 사용자 객체를 생성합니다.
     *
     * <p>변경된 사용자 정보는 db에 반영됩니다.
     * <p>닉네임 항목이 수정 대상입니다.
     * <p>요청 값이 null인 필드는 기존 값을 유지합니다.
     *
     * @param uuid    프로필을 수정할 사용자
     * @param request 수정할 사용자 프로필 정보
     */
    public void updateProfile(UUID uuid, UpdateProfileRequest request) {
        User originalUser = UserConverter.toDomain(userJpaRepository.findByUuid(uuid).get());
        User updatedUser = User.builder()
                .uuid(uuid)
                .nickname(request.getNickname() != null ? request.getNickname() : originalUser.getNickname())
                .name(originalUser.getName())
                .phoneNumber(originalUser.getPhoneNumber())
                .address(originalUser.getAddress())
                .email(originalUser.getEmail())
                .scope(originalUser.getScope())
                .build();
        userJpaRepository.save(UserConverter.toEntity(updatedUser));
    }

    /**
     * 사용자의 개인정보를 수정합니다.
     *
     * <p>주어진 UUID를 통해 기존 사용자를 조회한 후, 요청값이 존재하는 필드만 기존 정보와 병합하여 새 사용자 객체를 생성합니다.
     *
     * <p>변경된 사용자 정보는 db에 반영됩니다.
     * <p>이름, 전화번호, 주소, 이메일 항목이 수정 대상입니다.
     * <p>요청 값이 null인 필드는 기존 값을 유지합니다.
     *
     * @param uuid    프로필을 수정할 사용자
     * @param request 수정할 사용자 프로필 정보
     */
    public void updatePrivacy(UUID uuid, UpdatePrivacyRequest request) {
        User originalUser = UserConverter.toDomain(userJpaRepository.findByUuid(uuid).get());
        User updatedUser = User.builder()
                .nickname(originalUser.getNickname())
                .name(request.getName() != null ? request.getName() : originalUser.getName())
                .phoneNumber(request.getPhoneNumber() != null ? request.getPhoneNumber() : originalUser.getPhoneNumber())
                .address(request.getAddress() != null ? request.getAddress() : originalUser.getAddress())
                .email(request.getEmail() != null ? request.getEmail() : originalUser.getEmail())
                .scope(originalUser.getScope())
                .build();
        userJpaRepository.save(UserConverter.toEntity(updatedUser));
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
        userJpaRepository.deleteByUuid(uuid);
        authJpaRepository.deleteByUuid(uuid);
    }
}
