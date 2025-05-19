package oridungjeol.duckhang.auth.infrastructure.oauth2;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.auth.infrastructure.entity.JwtToken;
import oridungjeol.duckhang.auth.infrastructure.jwt.JwtGenerator;
import oridungjeol.duckhang.auth.infrastructure.repository.JwtJpaRepository;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${jwt.access-token-validity-in-milliseconds}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token-validity-in-milliseconds}")
    private long refreshTokenValidityInMilliseconds;

    private final JwtJpaRepository jwtJpaRepository;
    private final JwtGenerator jwtGenerator;

    /**
     * OAuth2 인증 성공 시 accessToken 및 refreshToken을 발급하고 쿠키에 저장합니다.
     * refreshToken은 DB에도 저장합니다.
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param authentication 인증 정보
     * @throws IOException IO 오류
     * @throws ServletException 서블릿 오류
     */
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        String jwtAccessToken = jwtGenerator.createAccessToken(principal);
        Cookie accessCookie = new Cookie("accessToken", jwtAccessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setSecure(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge((int) accessTokenValidityInMilliseconds / 1000);

        String jwtRefreshToken = jwtGenerator.createRefreshToken(principal);
        Cookie refreshCookie = new Cookie("refreshToken", jwtRefreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setSecure(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) refreshTokenValidityInMilliseconds / 1000);

        jwtJpaRepository.save(JwtToken.builder()
                .uuid(principal.getName())
                .refreshToken(jwtRefreshToken)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenValidityInMilliseconds / 1000))
                .build());

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
        response.sendRedirect("/user/profile");
    }
}