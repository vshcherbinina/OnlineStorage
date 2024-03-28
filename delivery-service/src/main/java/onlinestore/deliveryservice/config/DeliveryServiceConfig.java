package onlinestore.deliveryservice.config;

import onlinestore.deliveryservice.event.InventoryDocumentEvent;
import onlinestore.deliveryservice.event.OrderStatusEvent;
import onlinestore.deliveryservice.event.handler.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class DeliveryServiceConfig {
    private final EventHandler<InventoryDocumentEvent, OrderStatusEvent> orderInventedEventHandler;

    @Autowired
    public DeliveryServiceConfig(EventHandler<InventoryDocumentEvent, OrderStatusEvent> orderInventedEventHandler) {
        this.orderInventedEventHandler = orderInventedEventHandler;
    }

    @Bean
    public Function<InventoryDocumentEvent, OrderStatusEvent> inventoryDocumentProcessor() {
        return orderInventedEventHandler::handleEvent;
    }
}
