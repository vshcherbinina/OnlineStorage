package onlinestore.orderservice.event.consumer;

import onlinestore.orderservice.event.OrderStatusEvent;
import onlinestore.orderservice.model.repository.RepositoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Component
public class OrderStatusEventConsumer implements EventConsumer<OrderStatusEvent>{

    private final Scheduler jdbcScheduler;
    private final RepositoryUtil repositoryUtil;

    private static final Logger logger = LoggerFactory.getLogger(OrderStatusEventConsumer.class);

    @Autowired
    public OrderStatusEventConsumer(Scheduler jdbcScheduler, RepositoryUtil repositoryUtil) {
        this.jdbcScheduler = jdbcScheduler;
        this.repositoryUtil = repositoryUtil;
    }

    @Override
    public void consumeEvent(OrderStatusEvent event) {
        logger.info("New event received (order status change): {}", event.toString());
        Mono.fromRunnable(() -> updateOrderStatus(event))
                .subscribeOn(jdbcScheduler)
                .subscribe();
    }

    public void updateOrderStatus(OrderStatusEvent event) {
        repositoryUtil.updateOrderStatus(
                event.getOrderId(), event.getStatus(),
                event.getDateModified(), event.getStatusDescription());
    }

}
