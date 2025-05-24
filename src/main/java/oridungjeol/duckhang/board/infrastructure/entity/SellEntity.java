package oridungjeol.duckhang.board.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;
import oridungjeol.duckhang.board.domain.Board;
import oridungjeol.duckhang.board.domain.Purchase;
import oridungjeol.duckhang.board.domain.Sell;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "sell")
@Entity
public class SellEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long boardId;

    private int price;

    @Builder
    public SellEntity(Long boardId, int price) {
        this.boardId = boardId;
        this.price = price;
    }

    public Sell toDomain() {
        return new Sell(id, boardId, price);
    }

    public void updateFromDomain(Sell sell) {
        this.price = sell.getPrice();
    }
}
