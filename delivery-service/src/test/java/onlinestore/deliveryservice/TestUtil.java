package onlinestore.deliveryservice;

import onlinestore.deliveryservice.dto.OrderDetailDto;
import onlinestore.deliveryservice.event.InventoryDocumentEvent;
import onlinestore.deliveryservice.model.entity.DeliveryEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TestUtil {
    public static InventoryDocumentEvent getInstanceInventoryDocumentEvent() {
        InventoryDocumentEvent orderEvent = InventoryDocumentEvent.builder()
                .id(1L)
                .orderId(1L)
                .userName("petrov")
                .clientName("Petrov Pavel")
                .amount(BigDecimal.valueOf(50))
                .orderDataCreated(LocalDateTime.now())
                .destinationAddress("Saint Petersburg, Nevsky Avenue, 1")
                .description("test Petrov")
                .build();
        OrderDetailDto detail = OrderDetailDto.builder()
                .productArticle("Cream")
                .price(BigDecimal.valueOf(50))
                .quantity(1D)
                .amount(BigDecimal.valueOf(50))
                .build();
        orderEvent.setDetails(new ArrayList<>());
        orderEvent.getDetails().add(detail);
        return orderEvent;
    }

    public static DeliveryEntity getInstanceDeliveryEntity() {
        DeliveryEntity delivery = new DeliveryEntity(1L);
        delivery.setId(1L);
        return delivery;
    }
}
