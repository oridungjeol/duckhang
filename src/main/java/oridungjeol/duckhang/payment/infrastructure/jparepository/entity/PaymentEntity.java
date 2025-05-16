package oridungjeol.duckhang.payment.infrastructure.jparepository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
@Getter
@Setter
@NoArgsConstructor
public class PaymentEntity {

    @Id
    @Column(name = "order_id")
    private String orderId;


    @Column(name = "payment_key")
    private String paymentKey;

    private int amount;

    private String type;

    private String status;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "board_id")
    private Integer boardId;  // 연관관계 없이 단순 숫자 필드

    public PaymentEntity(String orderId, String paymentKey, int amount, int boardId, String type) {
    }
}
