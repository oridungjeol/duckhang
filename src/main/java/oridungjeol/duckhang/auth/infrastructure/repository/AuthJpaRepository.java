package oridungjeol.duckhang.auth.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.auth.infrastructure.entity.Auth;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthJpaRepository extends JpaRepository<Auth, String> {
    Optional<Auth> findByProviderId(String providerId);

    boolean existsByProviderId(String providerId);

    void deleteAuthByUuid(UUID uuid);

    Optional<Auth> findByUuid(UUID uuid);
}
