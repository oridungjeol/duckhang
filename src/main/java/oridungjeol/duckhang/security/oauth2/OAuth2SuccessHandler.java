package oridungjeol.duckhang.security.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${jwt.access-token-validity-in-milliseconds}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token-validity-in-milliseconds}")
    private long refreshTokenValidityInMilliseconds;

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
        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge((int) accessTokenValidityInMilliseconds / 1000);

        String refreshToken = jwtGenerator.createRefreshToken(customOAuth2User);
        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) refreshTokenValidityInMilliseconds / 1000);

        String providerId = customOAuth2User.getProviderId();
        Auth auth = authJpaRepository.findByProviderId(providerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 providerId의 Auth 정보가 존재하지 않습니다."));

        auth.setRefreshToken(refreshToken);
        authJpaRepository.save(auth);

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        response.sendRedirect("/user/profile");
    }
}
