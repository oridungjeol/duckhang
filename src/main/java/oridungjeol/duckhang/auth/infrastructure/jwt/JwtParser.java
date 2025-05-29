package oridungjeol.duckhang.auth.infrastructure.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtParser {

    @Value("${jwt.access-token-validity-in-milliseconds}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token-validity-in-milliseconds}")
    private long refreshTokenValidityInMilliseconds;

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("JWT secret key is missing");
        }
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * JWT 토큰에서 UUID(subject)를 추출합니다.
     *
     * @param token JWT 문자열
     * @return UUID 문자열
     */
    public String getUuid(String token) {
        return getClaims(token).getSubject();
    }

    /**
     * JWT 토큰에서 nickname을 추출합니다.
     *
     * @param token JWT 문자열
     * @return nickname 문자열
     */
    public String getNickname(String token) {
        return getClaims(token).get("nickname", String.class);
    }

    /**
     * JWT 토큰을 기반으로 CustomPrincipal 객체를 생성합니다.
     *
     * @param token JWT 문자열
     * @return CustomPrincipal 객체
     */
    public CustomPrincipal getPrincipal(String token) {
        UUID uuid = UUID.fromString(getUuid(token));
        String nickname = getNickname(token);
        return new CustomPrincipal(uuid, nickname);
    }

    /**
     * JWT 토큰의 발급 시간과 만료 시간을 검증합니다.
     *
     * @param token JWT 문자열
     * @return 유효하면 true, 그렇지 않으면 false
     */
    public Boolean validateToken(String token) {
        Date issuedAt = getClaims(token).getIssuedAt();
        Date expiration = getClaims(token).getExpiration();
        Date now = Date.from(Instant.now());
        return issuedAt.before(now) && expiration.after(now);
    }

    /**
     * JWT 토큰을 파싱하여 Claims 정보를 반환합니다.
     *
     * @param token JWT 문자열
     * @return Claims 객체
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
