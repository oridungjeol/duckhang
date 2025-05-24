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
        accessCookie.setHttpOnly(false);
        accessCookie.setSecure(false);
        accessCookie.setPath("/");
        accessCookie.setMaxAge((int) accessTokenValidityInMilliseconds / 1000);

        String jwtRefreshToken = jwtGenerator.createRefreshToken(principal);
        Cookie refreshCookie = new Cookie("refreshToken", jwtRefreshToken);
        refreshCookie.setHttpOnly(false);
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge((int) refreshTokenValidityInMilliseconds / 1000);

        jwtJpaRepository.save(JwtToken.builder()
                .uuid(principal.getName())
                .refreshToken(jwtRefreshToken)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenValidityInMilliseconds / 1000))
                .build());

        Cookie uuidCookie = new Cookie("uuid", principal.getName());
        uuidCookie.setPath("/");  // 모든 경로에서 접근 가능하도록
        uuidCookie.setHttpOnly(false); // 자바스크립트에서 접근하려면 false
        uuidCookie.setMaxAge(60 * 60 * 24); // 예: 1일 유지

        response.addCookie(uuidCookie);
        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);
//        response.sendRedirect(request.getHeader("referer"));
    }
}