package onlinestore.deliveryservice.event;

import lombok.Builder;
import lombok.Data;
import onlinestore.deliveryservice.model.entity.DeliveryEntity;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderStatusEvent implements Event {

    private static final String EVENT = "OrderStatus";

    private Long orderId;
    private LocalDateTime dateModified;
    private String status;
    private String statusDescription;

    public static OrderStatusEvent fromDeliveryStatusEntity(DeliveryEntity deliveryStatus) {
        return builder()
                .orderId(deliveryStatus.getOrderId())
                .status(deliveryStatus.getStatus().toString())
                .dateModified(deliveryStatus.getDateModified())
                .statusDescription(deliveryStatus.getDescription())
                .build();
    }
    @Override
    public String getEvent() {
        return EVENT;
    }
}
