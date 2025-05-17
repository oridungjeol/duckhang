package oridungjeol.duckhang.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.auth.infrastructure.entity.Auth;
import oridungjeol.duckhang.auth.infrastructure.repository.AuthJpaRepository;
import oridungjeol.duckhang.auth.support.converter.AuthConverter;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;
import oridungjeol.duckhang.user.support.UserConverter;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserJpaRepository userJpaRepository;
    private final AuthJpaRepository authJpaRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String provider;
        String providerId;
        String email;
        String nickname;

        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        provider = registrationId;
        providerId = attributes.get("id").toString();
        email = (String) kakaoAccount.get("email");
        nickname = (String) profile.get("nickname");

        Auth auth;
        User user;

        if (authJpaRepository.existsByProviderId(providerId)) {
            auth = authJpaRepository.save(Auth.builder()
                    .uuid(UUID.randomUUID())
                    .provider(provider)
                    .providerId(providerId)
                    .build());

            user = userJpaRepository.save(User.builder()
                    .uuid(auth.getUuid())
                    .nickname(nickname)
                    .email(email)
                    .scope(0)
                    .build());
        } else {
            auth = authJpaRepository.findByProviderId(providerId).get();
            user = userJpaRepository.findByUuid(auth.getUuid()).get();
        }

        return new CustomOAuth2User(UserConverter.toDomain(user), AuthConverter.toDomain(auth));
    }
}
