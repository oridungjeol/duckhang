package oridungjeol.duckhang.payment.infrastructure.jparepository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import oridungjeol.duckhang.board.RentalEntity;

@Table(name = "payment")
@Entity
@NoArgsConstructor
@Getter
@Setter
public class PaymentEntity {

    @Id
    private String orderId;
    private String paymentKey;
    private int amount;

    @OneToOne
    @JoinColumn(name = "board_id")  // payment 테이블의 board_id 컬럼이 외래키
    private RentalEntity rental;

    public PaymentEntity(String orderId, String paymentKey, int amount, RentalEntity rental) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.rental = rental;
    }
}
