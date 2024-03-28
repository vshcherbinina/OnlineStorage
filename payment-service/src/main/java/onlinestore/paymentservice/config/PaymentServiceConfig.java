package onlinestore.paymentservice.config;

import onlinestore.paymentservice.event.OrderEvent;
import onlinestore.paymentservice.event.OrderStatusEvent;
import onlinestore.paymentservice.event.handler.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class PaymentServiceConfig {

    private final EventHandler<OrderEvent, OrderStatusEvent> orderCreatedEventHandler;

    private final EventHandler<OrderStatusEvent, OrderEvent> paymentEventHandler;

    @Autowired
    public PaymentServiceConfig(EventHandler<OrderEvent, OrderStatusEvent> orderCreatedEventHandler, EventHandler<OrderStatusEvent, OrderEvent> paymentEventHandler) {
        this.orderCreatedEventHandler = orderCreatedEventHandler;
        this.paymentEventHandler = paymentEventHandler;
    }

    @Bean
    public Function<OrderEvent, OrderStatusEvent> orderCreatedEventProcessor() {
        return orderCreatedEventHandler::handleEvent;
    }

    @Bean
    public Function<OrderStatusEvent, OrderEvent> paymentEventSubscriber() {
        return paymentEventHandler::handleEvent;
    }

}
