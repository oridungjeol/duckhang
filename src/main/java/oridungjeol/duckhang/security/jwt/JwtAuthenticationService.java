package oridungjeol.duckhang.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {

    private final JwtParser jwtParser;

    public void authenticate(String token) {
        if (token != null && jwtParser.validateToken(token)) {
            String uuid = jwtParser.getUuid(token);
            String role = jwtParser.getRole(token);

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(uuid, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}
