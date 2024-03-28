package onlinestore.orderservice.config;

import onlinestore.orderservice.event.OrderEvent;
import onlinestore.orderservice.event.OrderStatusEvent;
import onlinestore.orderservice.event.consumer.EventConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Configuration
public class OrderServiceConfig {

    private final EventConsumer<OrderStatusEvent> orderStatusEventConsumer;

    @Autowired
    public OrderServiceConfig(EventConsumer<OrderStatusEvent> orderStatusEventConsumer) {
        this.orderStatusEventConsumer = orderStatusEventConsumer;
    }

    @Bean
    public Sinks.Many<OrderEvent> sink() {
        return Sinks.many()
                .multicast()
                .directBestEffort();
    }

    @Bean
    public Supplier<Flux<OrderEvent>> orderCreatedEventPublisher(
            Sinks.Many<OrderEvent> publisher) {
        return publisher::asFlux;
    }

    @Bean
    public Consumer<OrderStatusEvent> orderStatusEventProcessor() {
        return orderStatusEventConsumer::consumeEvent;
    }
}
