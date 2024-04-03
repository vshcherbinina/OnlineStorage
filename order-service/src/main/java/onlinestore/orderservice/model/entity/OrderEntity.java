package onlinestore.orderservice.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "order_spec")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String userName;
    @Column(columnDefinition = "numeric(15,2)", nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private String destinationAddress;
    private String description;
    @Column(nullable = false)
    private LocalDateTime dateCreated;
    @Column(nullable = false)
    private LocalDateTime dateModified;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @EqualsAndHashCode.Exclude
    @Transient
    private final transient List<OrderDetailEntity> details;

    public OrderEntity() {
        dateCreated = LocalDateTime.now();
        dateModified = dateCreated;
        status = OrderStatus.REGISTERED;
        details = new ArrayList<>();
    }

    public OrderEntity withUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public OrderEntity withAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public OrderEntity withDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
        return this;
    }

    public OrderEntity withDescription(String description) {
        this.description = description;
        return this;
    }

    public void updateStatus(OrderStatusHistoryEntity statusHistory) {
        status = statusHistory.getStatus();
        dateModified = statusHistory.getDateModified();
    }
}
