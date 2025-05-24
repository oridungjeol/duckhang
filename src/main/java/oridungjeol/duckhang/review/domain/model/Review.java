package oridungjeol.duckhang.review.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class Review {
    private final Long id;
    private final UUID targetId;
    private final Scope scope;
    private final Content content;
    private final UUID authorId;
    private final LocalDateTime createdAt;
    private final String orderId;

    private Review(
            Long id,
            UUID authorId,
            Scope scope,
            Content content,
            UUID targetId,
            LocalDateTime createdAt,
            String orderId
    ) {
        this.id = id;
        this.authorId = authorId;
        this.targetId = targetId;
        this.content = content;
        this.scope = scope;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }

    public static Review create(Long id, UUID targetId, Scope scope, Content content, UUID authorId, LocalDateTime createdAt, String orderId) {
        return new Review(id, targetId, scope, content, authorId, createdAt, orderId);
    }
}

