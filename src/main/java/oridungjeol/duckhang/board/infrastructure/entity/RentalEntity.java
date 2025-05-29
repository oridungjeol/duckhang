package oridungjeol.duckhang.board.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "rental")
@Entity
public class RentalEntity {

    @Id
    private Long boardId;

    private int price;

    private int deposit;

    @Builder
    public RentalEntity(Long boardId, int price, int deposit) {
        this.boardId = boardId;
        this.price = price;
        this.deposit = deposit;
    }
}