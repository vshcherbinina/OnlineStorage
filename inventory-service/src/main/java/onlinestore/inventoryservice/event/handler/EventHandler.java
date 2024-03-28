package onlinestore.inventoryservice.event.handler;

import onlinestore.inventoryservice.event.Event;

public interface EventHandler<T extends Event, R extends Event> {
    R handleEvent(T event);
}
