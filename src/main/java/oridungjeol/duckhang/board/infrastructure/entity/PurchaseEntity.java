package oridungjeol.duckhang.board.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "purchase")
@Entity
public class PurchaseEntity {
    @Id
    private Long boardId;

    private int price;

    @Builder
    public PurchaseEntity(Long boardId, int price) {
        this.boardId = boardId;
        this.price = price;
    }
}
