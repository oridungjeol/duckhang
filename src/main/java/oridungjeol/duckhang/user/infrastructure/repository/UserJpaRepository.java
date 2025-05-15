package oridungjeol.duckhang.user.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import oridungjeol.duckhang.user.infrastructure.entity.User;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUuid(UUID uuid);
}
