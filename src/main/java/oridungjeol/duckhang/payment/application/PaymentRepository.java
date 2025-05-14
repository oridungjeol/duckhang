package oridungjeol.duckhang.payment.application;

import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;

public interface PaymentRepository {
    void save(PaymentEntity paymentEntity);
}
