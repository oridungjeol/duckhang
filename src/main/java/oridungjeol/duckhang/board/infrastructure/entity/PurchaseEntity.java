package oridungjeol.duckhang.board.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Purchase;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "purchase")
@Entity
public class PurchaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;

    private int price;

    @Builder
    public PurchaseEntity(Long boardId, int price) {
        this.boardId = boardId;
        this.price = price;
    }

    public Purchase toDomain() {
        return new Purchase(id, boardId, price);
    }

    public void updateFromDomain(Purchase purchase) {
        this.price = purchase.getPrice();
    }
}
