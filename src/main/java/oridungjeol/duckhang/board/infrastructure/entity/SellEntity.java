package oridungjeol.duckhang.board.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sell")
@Entity
public class SellEntity {
    @Id
    private Long boardId;

    private int price;

    @Builder
    public SellEntity(Long boardId, int price) {
        this.boardId = boardId;
        this.price = price;
    }
}
