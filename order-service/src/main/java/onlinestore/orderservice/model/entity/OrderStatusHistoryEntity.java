package onlinestore.orderservice.model.entity;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity(name = "order_status_history")
public class OrderStatusHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "order_status_history_to_order_fk"), nullable = false)
    private OrderEntity order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime dateModified;
    private String statusDescription;

    public OrderStatusHistoryEntity(OrderEntity order, OrderStatus status, LocalDateTime dateModified) {
        this.order = order;
        this.status = status;
        this.dateModified = dateModified == null ? LocalDateTime.now() : dateModified;
    }

    public OrderStatusHistoryEntity(OrderEntity order) {
        this.order = order;
        this.status = order.getStatus();
        this.dateModified = order.getDateModified();
    }

}
