package oridungjeol.duckhang.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.security.oauth2.CustomOAuth2User;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtGenerator {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @Value("${jwt.access-token-validity-in-milliseconds}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.refresh-token-validity-in-milliseconds}")
    private long refreshTokenValidityInMilliseconds;

    @PostConstruct
    public void init() {
        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("JWT secret key is missing");
        }
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String createAccessToken(CustomOAuth2User customOAuth2User) {
        return createToken(customOAuth2User.getName(), customOAuth2User.getProvider(), accessTokenValidityInMilliseconds);
    }

    public String createRefreshToken(CustomOAuth2User customOAuth2User) {
        return createToken(customOAuth2User.getName(), customOAuth2User.getProvider(), refreshTokenValidityInMilliseconds);
    }

    private String createToken(String uuid, String provider, Long expiration) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(uuid)
                .claim("role", "ROLE_USER")
                .claim("provider", provider)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
