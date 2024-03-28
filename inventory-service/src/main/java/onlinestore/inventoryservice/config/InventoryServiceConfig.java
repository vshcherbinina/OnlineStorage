package onlinestore.inventoryservice.config;

import onlinestore.inventoryservice.event.InventoryDocumentEvent;
import onlinestore.inventoryservice.event.OrderEvent;
import onlinestore.inventoryservice.event.OrderStatusEvent;
import onlinestore.inventoryservice.event.handler.EventHandler;
import onlinestore.inventoryservice.model.entity.InventoryDetailEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
public class InventoryServiceConfig {

    private final EventHandler<OrderEvent, OrderStatusEvent> orderPaidEventHandler;

    @Autowired
    public InventoryServiceConfig(EventHandler<OrderEvent, OrderStatusEvent> orderPaidEventHandler) {
        this.orderPaidEventHandler = orderPaidEventHandler;
    }

    @Bean
    public Function<OrderEvent, OrderStatusEvent> orderPaidEventProcessor() {
        return orderPaidEventHandler::handleEvent;
    }

}
