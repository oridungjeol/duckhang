
package oridungjeol.duckhang.payment.infrastructure.jparepository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public PaymentEntity(String orderId, String paymentKey, int amount) {
        this.orderId = orderId;
        this.paymentKey = paymentKey;
        this.amount = amount;
    }
}
