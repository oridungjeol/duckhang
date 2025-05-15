package oridungjeol.duckhang.payment.support;

import oridungjeol.duckhang.payment.domain.Payment;
import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;

public class PaymentMapper {

    public static PaymentEntity toEntity(Payment payment) {
        PaymentEntity entity = new PaymentEntity();
        entity.setOrderId(payment.getOrderId());
        entity.setPaymentKey(payment.getPaymentKey());
        entity.setAmount(payment.getAmount());
        return entity;
    }

    public static Payment toDomain(PaymentEntity entity) {
        return new Payment(
                entity.getOrderId(),
                entity.getPaymentKey(),
                entity.getAmount()
        );
    }
}
