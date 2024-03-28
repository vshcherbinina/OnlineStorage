package onlinestore.deliveryservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.deliveryservice.event.InventoryDocumentEvent;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "delivery_status")
@NoArgsConstructor
@Data
public class DeliveryStatusEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;

    private LocalDateTime dateModified;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private String description;

    public DeliveryStatusEntity(InventoryDocumentEvent event) {
        this.orderId = event.getOrderId();
        this.dateModified = LocalDateTime.now();
        this.status = DeliveryStatus.DELIVERED;
        int random = (int) (Math.random()*4);
        if (random == 0) {
            this.status = DeliveryStatus.DELIVERY_FAILED;
            this.description = "Delivery failed... This happens 25% of 100%. Money is not returned :)";
        }
    }
}
