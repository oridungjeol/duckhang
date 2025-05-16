package oridungjeol.duckhang.payment.infrastructure.jparepository.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    private int boardId;

    private String type; // SELL, PURCHASE, DELEGATE, RENTAL 등

    private String status; // PENDING, PAID 등

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public PaymentEntity(String orderId, String paymentKey, int amount, int boardId, String type) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
        this.boardId = boardId;
        this.type = type;
        this.status = "PENDING";
    }
}
