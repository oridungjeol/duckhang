package oridungjeol.duckhang.review.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Review {
    private final Long id;
    private final UUID targetId;
    private final Scope scope;
    private final Content content;
    private final UUID authorId;
    private final LocalDateTime createdAt;
    private final String orderId;

    public static Review create(Long id, UUID targetId, Scope scope, Content content, UUID authorId, LocalDateTime createdAt, String orderId) {
        return new Review(id, targetId, scope, content, authorId, createdAt, orderId);
    }
}
