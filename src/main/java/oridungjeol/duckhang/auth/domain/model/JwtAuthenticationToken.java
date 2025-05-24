package oridungjeol.duckhang.auth.domain.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final CustomPrincipal principal;

    public JwtAuthenticationToken(CustomPrincipal principal) {
        super(principal.getAuthorities());
        this.principal = principal;
        setAuthenticated(true); // 이미 인증된 상태임을 명시
    }

    @Override
    public Object getCredentials() {
        return null; // JWT는 별도의 credentials 없음
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}

