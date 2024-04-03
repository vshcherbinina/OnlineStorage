package onlinestore.paymentservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity(name="transaction_acc")
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", foreignKey = @ForeignKey(name = "transaction_to_account_fk"), nullable = false)
    private AccountEntity account;

    @Column(nullable = false)
    private int income;
    @Column(columnDefinition = "numeric(15,2)")
    private BigDecimal amount;
    @Column(nullable = false)
    private LocalDateTime date;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", foreignKey = @ForeignKey(name = "transaction_to_payment_fk"))
    private PaymentEntity payment;

    public TransactionEntity(AccountEntity account, BigDecimal amount, int income) {
        this.account = account;
        this.income = income;
        this.amount = amount;
        this.date = LocalDateTime.now();
    }

    public TransactionEntity(PaymentEntity payment) {
        this.account = payment.getAccount();
        this.income = -1;
        this.amount = payment.getAmount();
        this.date = payment.getDateModified();
        this.payment = payment;
    }

}
