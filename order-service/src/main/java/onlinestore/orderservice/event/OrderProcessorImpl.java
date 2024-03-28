package onlinestore.orderservice.event;

import onlinestore.orderservice.model.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

@Component
public class OrderProcessorImpl implements OrderProcessor {

    private final Sinks.Many<OrderEvent> sink;

    @Autowired
    public OrderProcessorImpl(Sinks.Many<OrderEvent> sink) {
        this.sink = sink;
    }

    @Override
    public void process(OrderEntity order) {
        sink.emitNext(OrderEvent.fromOrder(order), Sinks.EmitFailureHandler.FAIL_FAST);
    }
}
