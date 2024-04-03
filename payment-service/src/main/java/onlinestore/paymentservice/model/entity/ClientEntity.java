package onlinestore.paymentservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity(name="client")
public class ClientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String userName;
    @Column(nullable = false)
    private String name;
    @Transient
    private AccountEntity account;

    public ClientEntity(String userName, String name) {
        this.userName = userName;
        this.name = name;
    }
}
