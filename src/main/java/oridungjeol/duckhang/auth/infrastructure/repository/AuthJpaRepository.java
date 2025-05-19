package oridungjeol.duckhang.auth.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.auth.infrastructure.entity.Auth;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthJpaRepository extends JpaRepository<Auth, UUID> {
    Optional<Auth> findByUuid(UUID uuid);

    Optional<Auth> findByProviderId(String providerId);

    Boolean existsByProviderId(String providerId);

    void deleteByUuid(UUID uuid);
}
