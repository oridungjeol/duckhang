package oridungjeol.duckhang.review.application.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ReviewResponseDto {
    private final Long id;
    private final Double scope;
    private final String content;
    private final UUID authorId;
    private final LocalDateTime createdAt;
    private final String orderId;

    public static ReviewResponseDto create(Long id, Double scope, String content, UUID authorId, LocalDateTime createdAt, String orderId) {
        return new ReviewResponseDto(id, scope, content, authorId, createdAt, orderId);
    }
}
