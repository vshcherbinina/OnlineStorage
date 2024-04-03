package onlinestore.paymentservice.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import onlinestore.paymentservice.dto.OrderStatusDto;
import onlinestore.paymentservice.event.OrderEvent;
import org.apache.commons.lang3.builder.ToStringExclude;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity(name = "order_data")
public class OrderDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String userName;
    @Column(columnDefinition = "numeric(15,2)")
    private BigDecimal amount;
    private String destinationAddress;
    private String description;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    @Enumerated(EnumType.STRING)
    private OrderStatusDto status;

    @EqualsAndHashCode.Exclude
    @Transient
    private transient List<OrderDetailEntity> details = new ArrayList<>();

    public static OrderDataEntity fromOrderEvent(OrderEvent orderEvent) {
        OrderDataEntity orderData = new OrderDataEntity();
        orderData.orderId = orderEvent.getId();
        orderData.userName = orderEvent.getUserName();
        orderData.amount = orderEvent.getAmount();
        orderData.destinationAddress = orderEvent.getDestinationAddress();
        orderData.description = orderEvent.getDescription();
        orderData.dateCreated = orderEvent.getDateCreated();
        orderData.dateModified = orderEvent.getDateModified();
        orderData.status = orderEvent.getStatus();
        orderEvent.getDetails().forEach(d -> orderData.details.add(OrderDetailEntity.fromOrderDetailDto(d)));
        orderData.details.forEach(d -> d.setOrderData(orderData));
        return orderData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderDataEntity orderData)) return false;
        return Objects.equals(id, orderData.id) && Objects.equals(orderId, orderData.orderId)
                && Objects.equals(userName, orderData.userName) && Objects.equals(amount, orderData.amount)
                && Objects.equals(destinationAddress, orderData.destinationAddress) && Objects.equals(description, orderData.description)
                && Objects.equals(dateCreated, orderData.dateCreated) && Objects.equals(dateModified, orderData.dateModified)
                && status == orderData.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, userName, amount, destinationAddress, description, dateCreated, dateModified, status);
    }

    @Override
    public String toString() {
        return "OrderDataEntity{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", userName='" + userName + '\'' +
                ", amount=" + amount +
                ", destinationAddress='" + destinationAddress + '\'' +
                ", description='" + description + '\'' +
                ", dateCreated=" + dateCreated +
                ", dateModified=" + dateModified +
                ", status=" + status +
                '}';
    }
}
