package onlinestore.inventoryservice.config;

import onlinestore.inventoryservice.event.OrderEvent;
import onlinestore.inventoryservice.event.OrderStatusEvent;
import onlinestore.inventoryservice.event.handler.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

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
