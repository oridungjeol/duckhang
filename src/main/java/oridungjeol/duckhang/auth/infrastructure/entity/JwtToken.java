package oridungjeol.duckhang.auth.infrastructure.entity;

import jakarta.persistence.Column;
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
public class JwtToken {
    @Id
    private String uuid;
    @Column(length = 512)
    private String refreshToken;
    private LocalDateTime expiresAt;
}
