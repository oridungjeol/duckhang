package oridungjeol.duckhang.review.infrastructure.converter;

import oridungjeol.duckhang.review.domain.model.Review;
import oridungjeol.duckhang.review.domain.model.Content;
import oridungjeol.duckhang.review.domain.model.Scope;

public class ReviewConverter {

    public static Review toDomain(oridungjeol.duckhang.review.infrastructure.entity.Review entity) {
        return Review.create(
                entity.getId(),
                entity.getTargetId(),
                new Scope(entity.getScope()),
                new Content(entity.getContent()),
                entity.getAuthorId(),
                entity.getCreatedAt(),
                entity.getOrderId()
        );
    }

    public static oridungjeol.duckhang.review.infrastructure.entity.Review toEntity(Review domain) {
        return oridungjeol.duckhang.review.infrastructure.entity.Review.builder()
                .id(domain.getId())
                .targetId(domain.getTargetId())
                .scope(domain.getScope().getValue())
                .content(domain.getContent().getValue())
                .authorId(domain.getAuthorId())
                .createdAt(domain.getCreatedAt())
                .orderId(domain.getOrderId())
                .build();
    }
}
