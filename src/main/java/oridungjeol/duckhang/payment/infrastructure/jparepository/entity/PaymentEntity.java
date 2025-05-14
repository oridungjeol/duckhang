
package oridungjeol.duckhang.payment.infrastructure.jparepository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "payment")
@Entity
@NoArgsConstructor
@Data
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
