package oridungjeol.duckhang.review.infrastructure.repository;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import oridungjeol.duckhang.review.application.dto.ScopeSumDto;
import oridungjeol.duckhang.review.infrastructure.entity.Review;

import java.util.UUID;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
    boolean existsByOrderId(String orderId);

    Page<Review> findByTargetId(UUID targetId, Pageable pageable);

    @Query("SELECT new oridungjeol.duckhang.review.application.dto.ScopeSumDto(SUM(r.scope), COUNT(r.scope)) " +
            "FROM Review r " +
            "WHERE r.targetId = :uuid")
    ScopeSumDto findScopeSumByTargetId(@Param("uuid") UUID uuid);
}
