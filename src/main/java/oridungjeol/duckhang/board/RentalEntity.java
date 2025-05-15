package oridungjeol.duckhang.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "rental")
public class RentalEntity {
    @Id
    @Column(name = "board_id")
    private int boardId;

    private int price;

    private int deposit;

    private LocalDate period;

}