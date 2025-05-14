package oridungjeol.duckhang.payment.infrastructure.jparepository;

import org.springframework.data.jpa.repository.JpaRepository;
import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;

public interface PaymentJpaRepository extends JpaRepository<PaymentEntity, String> {
}
