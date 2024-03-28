package onlinestore.orderservice.event.consumer;

import onlinestore.orderservice.event.Event;

public interface EventConsumer<T extends Event> {
    void consumeEvent(T event);
}
