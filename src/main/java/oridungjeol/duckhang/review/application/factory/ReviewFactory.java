package oridungjeol.duckhang.review.application.factory;

import org.springframework.stereotype.Component;
import oridungjeol.duckhang.review.application.dto.ReviewRequestDto;
import oridungjeol.duckhang.review.domain.model.Review;
import oridungjeol.duckhang.review.domain.model.Content;
import oridungjeol.duckhang.review.domain.model.Scope;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class ReviewFactory {

    public Review createNewReview(ReviewRequestDto dto, UUID uuid) {
        return Review.create(
                null,
                UUID.fromString(dto.getTargetId()),
                new Scope(dto.getScope()),
                new Content(dto.getContent()),
                uuid,
                LocalDateTime.now(),
                dto.getOrderId()
        );
    }
}
