package onlinestore.deliveryservice.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import onlinestore.deliveryservice.event.InventoryDocumentEvent;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "delivery")
@NoArgsConstructor
@Data
public class DeliveryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderId;
    @Column(nullable = false)
    private LocalDateTime dateModified;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;
    private String description;

    public DeliveryEntity(Long orderId) {
        this.orderId = orderId;
        this.dateModified = LocalDateTime.now();
        this.status = DeliveryStatus.DELIVERED;
        int random = (int) (Math.random()*4);
        if (random == 0) {
            this.status = DeliveryStatus.DELIVERY_FAILED;
            this.description = "Delivery failed... This happens 25% of 100%. Money is not returned :)";
        }
    }
}
