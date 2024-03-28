package onlinestore.inventoryservice.event;

import lombok.Builder;
import lombok.Data;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;

import java.time.LocalDateTime;

@Data
@Builder
public class OrderStatusEvent implements Event {

    private static final String EVENT = "OrderStatus";

    private Long orderId;
    private LocalDateTime dateModified;
    private String status;
    private String statusDescription;

    public static OrderStatusEvent fromInventoryDocumentEntity(InventoryDocumentEntity document) {
        return builder()
                .orderId(document.getOrderId())
                .status(document.getStatus().toString())
                .dateModified(document.getDateModified())
                .statusDescription(document.getStatusDescription())
                .build();
    }

    @Override
    public String getEvent() {
        return EVENT;
    }
}
