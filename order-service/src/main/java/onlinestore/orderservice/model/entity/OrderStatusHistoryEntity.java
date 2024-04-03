package onlinestore.orderservice.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@Entity(name = "order_status_history")
public class OrderStatusHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude @EqualsAndHashCode.Exclude @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "order_status_history_to_order_fk"), nullable = false)
    private OrderEntity order;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;
    @Column(nullable = false)
    private LocalDateTime dateModified;
    private String statusDescription;

    public OrderStatusHistoryEntity(OrderEntity order) {
        this.order = order;
        this.status = order.getStatus();
        this.dateModified = order.getDateModified();
    }

}
