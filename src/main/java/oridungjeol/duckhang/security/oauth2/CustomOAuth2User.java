package oridungjeol.duckhang.security.oauth2;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import oridungjeol.duckhang.auth.domain.model.Auth;
import oridungjeol.duckhang.user.domain.User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class CustomOAuth2User implements OAuth2User {

    private final User user;
    private final Auth auth;

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of("id", auth.getProviderId());
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getName() {
        return getUuid().toString();
    }

    private UUID getUuid() {
        return user.getUuid();
    }

    public String getNickname() {
        return user.getNickname();
    }

    public String getEmail() {
        return user.getEmail();
    }

    public String getProvider() {
        return auth.getProvider();
    }

    public String getProviderId() {
        return auth.getProviderId();
    }
}
