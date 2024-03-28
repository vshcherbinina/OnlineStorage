package onlinestore.paymentservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@Entity(name = "payment")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "payment_to_account_fk"))
    private AccountEntity account;

    private String currency;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @Column(nullable = false)
    private LocalDateTime dateModified;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(nullable = false)
    private Long orderId;

    private String statusDescription;

    public PaymentEntity(Double amount, Long orderId) {
        this.amount = amount;
        this.orderId = orderId;
        setStatusAndDateModification(PaymentStatus.CREATED);
        this.dateCreated = this.dateModified;
    }

    public void setStatusAndDateModification(PaymentStatus status) {
        this.status = status;
        this.dateModified = LocalDateTime.now();
    }

}
