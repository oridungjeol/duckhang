package oridungjeol.duckhang.review.application.usecase;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import oridungjeol.duckhang.review.application.dto.ReviewRequestDto;
import oridungjeol.duckhang.review.application.dto.ReviewResponseDto;
import oridungjeol.duckhang.review.application.factory.ReviewFactory;
import oridungjeol.duckhang.review.application.service.ReviewStatisticService;
import oridungjeol.duckhang.review.application.service.ReviewValidationService;
import oridungjeol.duckhang.review.domain.model.Review;
import oridungjeol.duckhang.review.infrastructure.converter.ReviewConverter;
import oridungjeol.duckhang.review.infrastructure.repository.ReviewJpaRepository;
import oridungjeol.duckhang.user.application.factory.UserFactory;
import oridungjeol.duckhang.user.domain.User;
import oridungjeol.duckhang.user.infrastructure.repository.UserJpaRepository;
import oridungjeol.duckhang.user.support.UserConverter;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewUseCase {

    private final ReviewJpaRepository reviewJpaRepository;
    private final ReviewFactory reviewFactory;
    private final ReviewStatisticService reviewStatisticService;
    private final ReviewValidationService reviewValidationService;
    private final UserFactory userFactory;
    private final UserJpaRepository userJpaRepository;

    /**
     * 사용자가 특정 대상에게 리뷰와 별점을 작성합니다.
     *
     * <p>리뷰는 0.0점부터 5.0점까지 0.5점 단위로만 작성할 수 있으며,
     * 사용자는 해당 사용자와 완료(PAID)된 거래 이력이 있어야 하고,
     * 동일 거래에 대해 중복 리뷰는 허용되지 않습니다.</p>
     * <p>작성한 리뷰는 평균을 재계산하여 user 테이블에 반영됩니다.</p>
     *
     * @param uuid          리뷰 작성자 ID
     * @param reviewRequest 리뷰 작성 요청 DTO (타겟 ID, 점수, 내용, 거래 ID 포함)
     * @return 리뷰 대상 사용자의 UUID
     * @throws IllegalArgumentException 거래가 없거나, 상태가 완료되지 않았거나, 중복 리뷰인 경우
     */
    public String addReview(UUID uuid, ReviewRequestDto reviewRequest) {
        reviewValidationService.validateWritable(reviewRequest.getOrderId());
        Review reviewDomain = reviewFactory.createNewReview(reviewRequest, uuid);
        reviewJpaRepository.save(ReviewConverter.toEntity(reviewDomain));

        double updatedAverage = reviewStatisticService.calculateAverageScope(UUID.fromString(reviewRequest.getTargetId()), reviewRequest.getScope());
        User oldUser = UserConverter.toDomain(userJpaRepository.findByUuid(uuid).get());
        User updatedUser = userFactory.renewUserScope(oldUser, updatedAverage);
        userJpaRepository.save(UserConverter.toEntity(updatedUser));

        return String.valueOf(uuid);
    }

    /**
     * 특정 사용자에 대한 리뷰를 불러옵니다.
     *
     * @param targetId 조회할 대상의 uuid
     * @param pageNumber 조회할 리뷰의 페이지
     * @param pageSize 조회할 리뷰의 갯수
     * @return 리뷰 목록
     */
    public List<ReviewResponseDto> getReviews(String targetId, int pageNumber, int pageSize) {
        return reviewJpaRepository
                .findByTargetId(UUID.fromString(targetId), PageRequest.of(
                                pageNumber,
                                pageSize,
                                Sort.by("createdAt").descending()
                        )
                )
                .stream()
                .map(ReviewFactory::toReviewResponse)
                .toList();
    }
}
