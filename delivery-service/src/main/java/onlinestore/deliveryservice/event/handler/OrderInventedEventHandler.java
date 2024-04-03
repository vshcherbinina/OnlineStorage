package onlinestore.deliveryservice.event.handler;

import onlinestore.deliveryservice.event.InventoryDocumentEvent;
import onlinestore.deliveryservice.event.OrderStatusEvent;
import onlinestore.deliveryservice.model.entity.DeliveryEntity;
import onlinestore.deliveryservice.model.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OrderInventedEventHandler implements EventHandler<InventoryDocumentEvent, OrderStatusEvent> {

    private final DeliveryRepository deliveryRepository;

    @Autowired
    public OrderInventedEventHandler(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    @Transactional
    public OrderStatusEvent handleEvent(InventoryDocumentEvent event) {
        DeliveryEntity deliveryStatus = new DeliveryEntity(event.getOrderId());
        deliveryRepository.save(deliveryStatus);
        return OrderStatusEvent.fromDeliveryStatusEntity(deliveryStatus);
    }
}
