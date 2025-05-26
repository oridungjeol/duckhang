package oridungjeol.duckhang.review.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.payment.application.PaymentRepository;
import oridungjeol.duckhang.payment.infrastructure.jparepository.entity.PaymentEntity;
import oridungjeol.duckhang.review.infrastructure.repository.ReviewJpaRepository;

@Service
@RequiredArgsConstructor
public class ReviewValidationService {

    private final ReviewJpaRepository reviewJpaRepository;
    private final PaymentRepository paymentJpaRepository;

    /**
     * 주어진 거래 ID(orderId)에 대해 리뷰를 작성할 수 있는 조건을 검증합니다.
     *
     * <p>검증 조건:</p>
     * <ul>
     *     <li>해당 거래(orderId)가 존재해야 합니다. 존재하지 않을 경우 {@link IllegalArgumentException}을 발생시킵니다.</li>
     *     <li>거래 상태가 "PAID" 또는 "CANCELED"가 아닌 경우에만 리뷰 작성을 허용합니다.
     *         그렇지 않으면 {@link IllegalArgumentException}을 발생시킵니다.</li>
     *     <li>해당 거래에 이미 리뷰가 존재할 경우 리뷰 중복 작성을 방지하기 위해 예외를 발생시킵니다.</li>
     * </ul>
     *
     * @param orderId 검증할 거래의 고유 ID
     * @throws IllegalArgumentException 거래가 존재하지 않거나, 리뷰 가능 조건을 만족하지 않을 경우
     */
    public void validateWritable(String orderId) {
        PaymentEntity payment = paymentJpaRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 거래입니다."));

        if ("PAID".equals(payment.getStatus()) || "CANCELED".equals(payment.getStatus())) {
            throw new IllegalArgumentException("완료된 거래에 한해서만 리뷰를 남길 수 있습니다.");
        }

        if (reviewJpaRepository.existsByOrderId(orderId)) {
            throw new IllegalArgumentException("이미 리뷰를 남긴 거래입니다.");
        }
    }
}
