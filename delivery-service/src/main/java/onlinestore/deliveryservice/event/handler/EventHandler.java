package onlinestore.deliveryservice.event.handler;

import onlinestore.deliveryservice.event.Event;

public interface EventHandler<T extends Event, R extends Event> {
    R handleEvent(T event);
}
