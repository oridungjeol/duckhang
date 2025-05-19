package oridungjeol.duckhang.auth.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OAuthToken {
    @Id
    private String uuid;
    private String accessToken;
    private LocalDateTime expiresAt;
}
