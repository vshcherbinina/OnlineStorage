package onlinestore.orderservice.event;

import onlinestore.orderservice.model.entity.OrderEntity;

public interface OrderProcessor {
    void process(OrderEntity order);
}
