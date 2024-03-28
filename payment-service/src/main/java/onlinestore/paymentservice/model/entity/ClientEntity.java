package onlinestore.paymentservice.model.entity;

import lombok.*;

import javax.persistence.*;

@Data
@ToString
@NoArgsConstructor
@Entity(name="client")
public class ClientEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    private String name;

    @Transient
    private AccountEntity account;

    public ClientEntity(String userName, String name) {
        this.userName = userName;
        this.name = name;
    }

}
