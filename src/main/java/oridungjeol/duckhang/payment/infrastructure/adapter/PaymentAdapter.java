package oridungjeol.duckhang.payment.infrastructure.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.payment.application.PaymentRepository;
import oridungjeol.duckhang.payment.infrastructure.jparepository.PaymentJpaRepository;
import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PaymentAdapter implements PaymentRepository {
    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public void save(PaymentEntity paymentEntity) {
        paymentJpaRepository.save(paymentEntity);
    }

    @Override
    public Optional<PaymentEntity> findByOrderId(String orderId) {
        return paymentJpaRepository.findById(orderId);
    }
}
