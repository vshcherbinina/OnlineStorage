package onlinestore.orderservice.event;

import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.orderservice.model.entity.OrderStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class OrderStatusEvent implements Event {

    private static final String EVENT = "OrderStatus";

    private Long orderId;
    private LocalDateTime dateModified;
    private OrderStatus status;
    private String statusDescription;

    @Override
    public String getEvent() {
        return EVENT;
    }
}
