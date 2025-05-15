package oridungjeol.duckhang.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.auth.infrastructure.entity.Auth;
import oridungjeol.duckhang.auth.infrastructure.repository.AuthJpaRepository;
import oridungjeol.duckhang.security.jwt.JwtGenerator;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final AuthJpaRepository authJpaRepository;
    private final JwtGenerator jwtGenerator;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String accessToken = jwtGenerator.createAccessToken(customOAuth2User);
        String refreshToken = jwtGenerator.createRefreshToken(customOAuth2User);

        String providerId = customOAuth2User.getProviderId();
        Auth auth = authJpaRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 providerId의 Auth 정보가 존재하지 않습니다."));

        auth.setRefreshToken(refreshToken);
        authJpaRepository.save(auth);

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.sendRedirect("/user/profile");
    }
}
