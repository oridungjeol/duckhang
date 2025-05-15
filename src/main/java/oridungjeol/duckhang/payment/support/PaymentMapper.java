package oridungjeol.duckhang.payment.support;

import oridungjeol.duckhang.payment.domain.Payment;
import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;

public class PaymentMapper {

    public static PaymentEntity toEntity(Payment payment) {
        return new PaymentEntity(
                payment.getOrderId(),
                payment.getPaymentKey(),
                payment.getAmount(),
                payment.getBoardId(),
                payment.getType()
        );
    }
}
