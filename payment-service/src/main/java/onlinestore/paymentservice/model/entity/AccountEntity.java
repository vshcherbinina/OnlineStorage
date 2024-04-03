package onlinestore.paymentservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity(name="account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @EqualsAndHashCode.Exclude @ToString.Exclude @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", foreignKey = @ForeignKey(name = "account_to_client_fk"), nullable = false)
    private ClientEntity client;

    @Column(nullable = false)
    private String number;
    private String currency;
    @Column(columnDefinition = "numeric(15,2)", nullable = false)
    private BigDecimal balance;
    private LocalDateTime dateCreated;

    public final static String CURRENCY = "RUB";
    private final static String MASK = "%1$09d";

    public AccountEntity(ClientEntity client) {
        this.client = client;
        this.number = String.format(MASK, client.getId());;
        this.currency = CURRENCY;
        this.dateCreated = LocalDateTime.now();
        this.balance = BigDecimal.valueOf(0);
    }

}
