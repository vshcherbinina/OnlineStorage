package onlinestore.paymentservice.event.handler;


import onlinestore.paymentservice.dto.OrderStatusDto;
import onlinestore.paymentservice.event.OrderEvent;
import onlinestore.paymentservice.event.OrderStatusEvent;
import onlinestore.paymentservice.model.entity.ClientEntity;
import onlinestore.paymentservice.model.util.RepositoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import javax.transaction.Transactional;

@Component
public class PaymentEventHandler implements EventHandler<OrderStatusEvent, OrderEvent> {

    private final RepositoryUtil repositoryUtil;

    private final Scheduler scheduler;

    private static final int PAYMENT_SLEEP_MS = 2000;

    private final Logger logger = LoggerFactory.getLogger(PaymentEventHandler.class);

    @Autowired
    public PaymentEventHandler(RepositoryUtil repositoryUtil, Scheduler scheduler) {
        this.repositoryUtil = repositoryUtil;
        this.scheduler = scheduler;
    }

    @Override
    @Transactional
    public OrderEvent handleEvent(OrderStatusEvent event) {
        logger.info("Event processing (change of order status from id={} to '{}')"
                , event.getOrderId(), event.getStatus());
        Mono.fromRunnable(() -> repositoryUtil.updateOrderData(event))
                .subscribeOn(scheduler)
                .subscribe();
        if (event.getStatus().toString().equals(OrderStatusDto.PAID.toString())) {
            try {
                Thread.sleep(PAYMENT_SLEEP_MS);
            } catch (Exception e) {
                logger.warn("An error occurred while processing the event (change of order status with id={} to '{}'): {}"
                        , event.getOrderId(), event.getStatus(), e.getMessage());
            }
            return repositoryUtil.getOrderData(event.getOrderId());
        } else if (event.getStatus().toString().equals(OrderStatusDto.INVENTION_FAILED.toString())) {
            repositoryUtil.declinePaymentByOrderId(event.getOrderId(), event.getStatusDescription());
        }
        return null;
    }

}
