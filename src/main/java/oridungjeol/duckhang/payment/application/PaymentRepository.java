package oridungjeol.duckhang.payment.application;

import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;

import java.util.Optional;

public interface PaymentRepository {
    void save(PaymentEntity paymentEntity);
    Optional<PaymentEntity> findByOrderId(String orderId);

}
