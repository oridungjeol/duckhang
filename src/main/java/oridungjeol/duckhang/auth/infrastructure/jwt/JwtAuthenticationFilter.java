package oridungjeol.duckhang.auth.infrastructure.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;
import oridungjeol.duckhang.auth.infrastructure.repository.JwtJpaRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtGenerator jwtGenerator;
    private final JwtParser jwtParser;
    private final JwtJpaRepository jwtJpaRepository;

    /**
     * JWT 기반 인증 처리를 수행합니다.
     * <ul>
     *     <li>Access Token 및 Refresh Token을 쿠키에서 추출</li>
     *     <li>Access Token이 유효하지 않으면 Refresh Token으로 검증</li>
     *     <li>Refresh Token이 유효하고 DB에 존재할 경우 Access Token 재발급</li>
     *     <li>인증 정보를 SecurityContextHolder에 설정</li>
     * </ul>
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        Map<String, String> token = resolveToken(request);
        if (token.isEmpty()) {
            response.sendRedirect("http://localhost:3000/login");
        } else {
            String accessToken = token.get("accessToken");
            String refreshToken = token.get("refreshToken");
            if (!jwtParser.validateToken(accessToken) && jwtJpaRepository.existsByRefreshToken(refreshToken)) {
                accessToken = jwtGenerator.createAccessToken(jwtParser.getPrincipal(refreshToken));
            }
            authenticate(accessToken);
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 요청 쿠키에서 Access Token과 Refresh Token을 추출합니다.
     *
     * @param request HttpServletRequest
     * @return token map (accessToken, refreshToken)
     */
    private Map<String, String> resolveToken(HttpServletRequest request) {
        Map<String, String> tokens = new HashMap<>();

        if (request.getCookies() == null) return tokens;

        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                tokens.put("accessToken", cookie.getValue());
            } else if ("refreshToken".equals(cookie.getName())) {
                tokens.put("refreshToken", cookie.getValue());
            }
        }

        return tokens;
    }

    /**
     * JWT에서 사용자 정보를 추출하여 인증 객체를 생성하고 SecurityContext에 등록합니다.
     *
     * @param token JWT 문자열
     */
    private void authenticate(String token) {
        CustomPrincipal customPrincipal = new CustomPrincipal(UUID.fromString(jwtParser.getUuid(token)), jwtParser.getNickname(token));
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                customPrincipal,
                null,
                customPrincipal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
