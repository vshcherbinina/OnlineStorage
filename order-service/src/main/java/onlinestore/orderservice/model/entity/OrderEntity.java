package onlinestore.orderservice.model.entity;

import lombok.Data;

import javax.persistence.*;
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

    private Double amount;

    private String destinationAddress;

    private String description;

    @Column(nullable = false)
    private LocalDateTime dateCreated;

    @Column(nullable = false)
    private LocalDateTime dateModified;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

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

    public OrderEntity withAmount(Double amount) {
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
