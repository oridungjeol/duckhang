package oridungjeol.duckhang.review.infrastructure.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private UUID targetId;
    private Double scope;
    private String content;
    private UUID authorId;
    private LocalDateTime createdAt;
    private String orderId;

    private Review(Long id, UUID authorId, UUID targetId, String content, Double scope, String orderId, LocalDateTime createdAt) {
        this.id = id;
        this.authorId = authorId;
        this.targetId = targetId;
        this.content = content;
        this.scope = scope;
        this.orderId = orderId;
        this.createdAt = createdAt;
    }
}
