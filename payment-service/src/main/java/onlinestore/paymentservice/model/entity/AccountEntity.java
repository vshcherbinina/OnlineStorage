package onlinestore.paymentservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@ToString
@NoArgsConstructor
@Entity(name="account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "account_to_client_fk"), nullable = false)
    private ClientEntity client;
    private String number;
    private String currency;
    private Double balance;
    private LocalDateTime dateCreated;

    @Transient
    public final static String CURRENCY = "RUB";
    @Transient
    private final static String MASK = "%1$04d";

    public AccountEntity(ClientEntity client) {
        this.client = client;
        this.number = String.format(MASK, client.getId());;
        this.currency = CURRENCY;
        this.dateCreated = LocalDateTime.now();
        this.balance = 0.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountEntity account)) return false;
        return Objects.equals(id, account.id) && Objects.equals(client, account.client) && Objects.equals(number, account.number) && Objects.equals(currency, account.currency) && Objects.equals(balance, account.balance) && Objects.equals(dateCreated, account.dateCreated);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, client, number, currency, balance, dateCreated);
    }
}
