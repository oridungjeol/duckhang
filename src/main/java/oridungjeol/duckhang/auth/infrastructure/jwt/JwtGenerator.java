package oridungjeol.duckhang.auth.infrastructure.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.auth.domain.model.CustomPrincipal;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtGenerator {

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
     * 주어진 CustomPrincipal을 기반으로 accessToken을 생성합니다.
     * @param principal 인증 정보 객체
     * @return accessToken 문자열
     */
    public String createAccessToken(CustomPrincipal principal) {
        return createToken(principal, accessTokenValidityInMilliseconds);
    }

    /**
     * 주어진 CustomPrincipal을 기반으로 refreshToken을 생성합니다.
     * @param principal 인증 정보 객체
     * @return refreshToken 문자열
     */
    public String createRefreshToken(CustomPrincipal principal) {
        return createToken(principal, refreshTokenValidityInMilliseconds);
    }

    /**
     * 주어진 principal 정보와 유효 시간으로 JWT 토큰을 생성합니다.
     * @param principal 사용자 정보
     * @param validation 유효 시간 (ms)
     * @return 생성된 JWT 문자열
     */
    private String createToken(CustomPrincipal principal, long validation) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(principal.getName())
                .claim("nickname", principal.getNickname())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(validation)))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }
}
