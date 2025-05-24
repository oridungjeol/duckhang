package oridungjeol.duckhang.review.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.review.infrastructure.entity.Review;

public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
    boolean existsByOrderId(String orderId);
}
