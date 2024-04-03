package onlinestore.paymentservice.event.handler;

import lombok.extern.slf4j.Slf4j;
import onlinestore.paymentservice.dto.OrderStatusDto;
import onlinestore.paymentservice.event.OrderEvent;
import onlinestore.paymentservice.event.OrderStatusEvent;
import onlinestore.paymentservice.model.entity.AccountEntity;
import onlinestore.paymentservice.model.entity.PaymentEntity;
import onlinestore.paymentservice.model.util.RepositoryUtil;
import onlinestore.paymentservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
public class OrderCreatedEventHandler implements EventHandler<OrderEvent, OrderStatusEvent> {

    private final ClientService clientService;
    private final RepositoryUtil repositoryUtil;

    @Autowired
    public OrderCreatedEventHandler(ClientService clientService, RepositoryUtil repositoryUtil) {
        this.clientService = clientService;
        this.repositoryUtil = repositoryUtil;
    }

    @Override
    @Transactional
    public OrderStatusEvent handleEvent(OrderEvent event) {
        PaymentEntity payment = new PaymentEntity(event.getAmount(), event.getId());
        try {
            repositoryUtil.saveOrderData(event);
            AccountEntity account = clientService.getClientByUserName(event.getUserName()).getAccount();
            payment.setAccount(account);
            payment.setCurrency(account.getCurrency());
            PaymentEntity savedPayment = repositoryUtil.getPaymentRepository().save(payment);
            payment.setId(savedPayment.getId());
            repositoryUtil.deductUserBalance(payment);
        } catch (Exception e) {
            log.error("Error handle event order created (id={}): {}", event.getId(), e.getMessage());
            return OrderStatusEvent.builder()
                    .orderId(event.getId())
                    .status(OrderStatusDto.PAYMENT_FAILED)
                    .dateModified(LocalDateTime.now())
                    .statusDescription(e.getMessage())
                    .build();
        }
        return OrderStatusEvent.fromPaymentEntity(payment);
    }


}
