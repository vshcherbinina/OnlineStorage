package onlinestore.paymentservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import onlinestore.paymentservice.dto.OrderStatusDto;
import onlinestore.paymentservice.event.OrderEvent;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@Entity(name = "order_data")
public class OrderDataEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private String userName;
    private Double amount;
    private String destinationAddress;
    private String description;
    private LocalDateTime dateCreated;
    private LocalDateTime dateModified;
    @Enumerated(EnumType.STRING)
    private OrderStatusDto status;

    @Transient
    private List<OrderDetailEntity> details;

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
        orderData.details = new ArrayList<>();
        orderEvent.getDetails().forEach(d -> orderData.details.add(OrderDetailEntity.fromOrderDetailDto(d)));
        orderData.details.forEach(d -> d.setOrderData(orderData));
        return orderData;
    }

}
