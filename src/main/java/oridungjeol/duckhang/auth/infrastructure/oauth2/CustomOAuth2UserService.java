package oridungjeol.duckhang.auth.infrastructure.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.auth.infrastructure.entity.Auth;
import oridungjeol.duckhang.auth.infrastructure.repository.AuthJpaRepository;
import oridungjeol.duckhang.user.infrastructure.entity.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;

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

        String provider = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> userInfo = oAuth2User.getAttributes();
        Map<String, Object> account = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) account.get("profile");

        Auth auth;
        User user;
        String providerId = userInfo.get("id").toString();
        if (authJpaRepository.existsByProviderId(providerId)) {
            auth = authJpaRepository.findByProviderId(providerId).get();
            user = userJpaRepository.findByUuid(auth.getUuid()).get();
        } else {
            auth = authJpaRepository.save(Auth.builder()
                    .uuid(UUID.randomUUID())
                    .provider(provider)
                    .providerId(providerId)
                    .build());

            user = userJpaRepository.save(User.builder()
                    .uuid(auth.getUuid())
                    .nickname(profile.get("nickname").toString())
                    .email(account.get("email").toString())
                    .scope(0)
                    .build());
        }

        return new CustomPrincipal(auth.getUuid(), user.getNickname());
    }
}
