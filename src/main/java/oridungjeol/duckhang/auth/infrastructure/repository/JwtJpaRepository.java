package oridungjeol.duckhang.auth.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.auth.infrastructure.entity.JwtToken;

public interface JwtJpaRepository extends JpaRepository<JwtToken, String> {
    boolean findByRefreshToken(String refreshToken);
}
