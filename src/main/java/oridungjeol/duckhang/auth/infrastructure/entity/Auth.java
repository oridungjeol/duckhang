package oridungjeol.duckhang.auth.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Auth {
    @Id
    private UUID uuid;
    @Column(nullable = false)
    private String provider;
    @Column(unique = true, nullable = false)
    private String providerId;
    @Column(unique = true)
    private String refreshToken;
}
