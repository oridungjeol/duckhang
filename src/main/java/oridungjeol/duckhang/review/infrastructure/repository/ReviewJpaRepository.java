package oridungjeol.duckhang.review.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.review.infrastructure.entity.Review;

import java.util.UUID;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
    boolean existsByOrderId(String orderId);

    Page<Review> findByTargetId(UUID targetId, Pageable pageable);
}
