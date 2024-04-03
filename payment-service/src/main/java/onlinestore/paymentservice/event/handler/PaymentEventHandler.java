package onlinestore.paymentservice.event.handler;


import lombok.extern.slf4j.Slf4j;
import onlinestore.paymentservice.dto.OrderStatusDto;
import onlinestore.paymentservice.event.OrderEvent;
import onlinestore.paymentservice.event.OrderStatusEvent;
import onlinestore.paymentservice.model.util.RepositoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

@Slf4j
@Component
public class PaymentEventHandler implements EventHandler<OrderStatusEvent, OrderEvent> {

    private final RepositoryUtil repositoryUtil;
    private final Scheduler scheduler;
    private static final int PAYMENT_SLEEP_MS = 2000;


    @Autowired
    public PaymentEventHandler(RepositoryUtil repositoryUtil, Scheduler scheduler) {
        this.repositoryUtil = repositoryUtil;
        this.scheduler = scheduler;
    }

    @Override
    @Transactional
    public OrderEvent handleEvent(OrderStatusEvent event) {
        log.info("Event processing (change of order status from id={} to '{}')"
                , event.getOrderId(), event.getStatus());
        Mono.fromRunnable(() -> repositoryUtil.updateOrderData(event))
                .subscribeOn(scheduler)
                .subscribe();
        try {
            Thread.sleep(PAYMENT_SLEEP_MS);
            if (event.getStatus().toString().equals(OrderStatusDto.PAID.toString())) {
                return repositoryUtil.getOrderData(event.getOrderId());
            } else if (event.getStatus().toString().equals(OrderStatusDto.INVENTION_FAILED.toString())) {
                repositoryUtil.declinePaymentByOrderId(event.getOrderId(), event.getStatusDescription());
            }
        } catch (Exception e) {
            log.error("Error while handle the event (change of order status with id={} to '{}'): {}"
                    , event.getOrderId(), event.getStatus(), e.getMessage());
        }
        return null;
    }

}
