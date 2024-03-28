package onlinestore.paymentservice.event.handler;

import onlinestore.paymentservice.event.Event;

public interface EventHandler<T extends Event, R extends Event> {
    R handleEvent(T event);
}
