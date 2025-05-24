package oridungjeol.duckhang.review.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.review.infrastructure.converter.ReviewConverter;
import oridungjeol.duckhang.review.application.dto.ReviewRequestDto;
import oridungjeol.duckhang.review.application.service.ReviewValidationService;
import oridungjeol.duckhang.review.application.factory.ReviewFactory;
import oridungjeol.duckhang.review.domain.model.Review;
import oridungjeol.duckhang.review.infrastructure.repository.ReviewJpaRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewUseCase {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewFactory reviewFactory;
    private final ReviewValidationService reviewValidationService;

    /**
     * 사용자가 특정 대상에게 리뷰와 별점을 작성합니다.
     *
     * <p>리뷰는 0.0점부터 5.0점까지 0.5점 단위로만 작성할 수 있으며,
     * 사용자는 해당 사용자와 완료(PAID)된 거래 이력이 있어야 하고,
     * 동일 거래에 대해 중복 리뷰는 허용되지 않습니다.</p>
     *
     * @param uuid          리뷰 작성자 ID
     * @param reviewRequest 리뷰 작성 요청 DTO (타겟 ID, 점수, 내용, 거래 ID 포함)
     * @return 리뷰 대상 사용자의 UUID
     * @throws IllegalArgumentException 거래가 없거나, 상태가 완료되지 않았거나, 중복 리뷰인 경우
     */
    public UUID addReview(UUID uuid, ReviewRequestDto reviewRequest) {
        reviewValidationService.validateWritable(reviewRequest.getOrderId());

        Review reviewDomain = reviewFactory.createNewReview(reviewRequest, uuid);

        oridungjeol.duckhang.review.infrastructure.entity.Review reviewEntity = ReviewConverter.toEntity(reviewDomain);
        reviewJpaRepository.save(reviewEntity);

        return reviewEntity.getTargetId();
    }
}
