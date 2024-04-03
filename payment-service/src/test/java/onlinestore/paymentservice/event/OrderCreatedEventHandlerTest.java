package onlinestore.paymentservice.event;

import lombok.extern.slf4j.Slf4j;
import onlinestore.paymentservice.TestUtil;
import onlinestore.paymentservice.config.JdbcConfig;
import onlinestore.paymentservice.config.RestTemplateConfig;
import onlinestore.paymentservice.dto.OrderStatusDto;
import onlinestore.paymentservice.event.handler.OrderCreatedEventHandler;
import onlinestore.paymentservice.model.entity.ClientEntity;
import onlinestore.paymentservice.model.entity.OrderDataEntity;
import onlinestore.paymentservice.model.entity.PaymentEntity;
import onlinestore.paymentservice.model.repository.*;
import onlinestore.paymentservice.model.util.LockByKey;
import onlinestore.paymentservice.model.util.RepositoryUtil;
import onlinestore.paymentservice.service.ClientService;
import onlinestore.paymentservice.service.ClientServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(OrderCreatedEventHandler.class)
public class OrderCreatedEventHandlerTest {

    @Autowired
    private OrderCreatedEventHandler orderCreatedEventHandler;
    @Autowired
    private ClientService clientService;
    @Autowired
    private RepositoryUtil repositoryUtil;
    @MockBean
    private TransactionRepository transactionRepository;
    @MockBean
    private PaymentRepository paymentRepository;
    @MockBean
    private ClientRepository clientRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private OrderDataRepository orderDataRepository;
    @MockBean
    private OrderDetailRepository orderDetailRepository;
    @MockBean
    private LockByKey lockByKey;
    @Autowired
    private JdbcConfig jdbcConfig;

    @Configuration
    @ComponentScan(basePackageClasses = {OrderCreatedEventHandler.class, ClientServiceImpl.class, RepositoryUtil.class,
            JdbcConfig.class, RestTemplateConfig.class})
    public static class TestConf {
    }

    private OrderEvent orderEvent;
    private OrderDataEntity orderData;
    private PaymentEntity payment;


    @BeforeEach
    public void setUp() {
        ClientEntity clientFirst = TestUtil.getInstanceClientFirst();
        orderEvent = TestUtil.getInstanceOrderEvent(clientFirst);
        orderData = OrderDataEntity.fromOrderEvent(orderEvent);
        orderData.setId(1L);
        payment = TestUtil.getInstancePaymentEntity(clientFirst);
    }

    @Test
    public void testHandleEvent() {
        orderEvent.setStatus(OrderStatusDto.REGISTERED);
        payment.getAccount().setBalance(orderEvent.getAmount());
        Mockito.when(orderDataRepository.save(any(OrderDataEntity.class)))
                .thenReturn(orderData);
        Mockito.when(clientRepository.findByUserName(any(String.class)))
                .thenReturn(Optional.of(payment.getAccount().getClient()));
        Mockito.when(accountRepository.findByClientId(any(Long.class)))
                .thenReturn(Optional.of(payment.getAccount()));
        Mockito.when(paymentRepository.save(any(PaymentEntity.class)))
                .thenReturn(payment);

        OrderStatusEvent orderStatusEvent = orderCreatedEventHandler.handleEvent(orderEvent);

        log.info("Order created event: {}, hash: {}", orderEvent, orderEvent.hashCode());
        log.info("Order status event (output): {}, hash: {}", orderStatusEvent, orderStatusEvent.hashCode());
        Assertions.assertThat(orderStatusEvent).isNotNull();
        Assertions.assertThat(orderStatusEvent.getStatus()).isEqualTo(OrderStatusDto.PAID);
    }

    @Test
    public void testHandleEventInsufficientBalance() {
        orderEvent.setStatus(OrderStatusDto.REGISTERED);
        payment.getAccount().setBalance(BigDecimal.valueOf(0D));
        Mockito.when(orderDataRepository.save(any(OrderDataEntity.class)))
                .thenReturn(orderData);
        Mockito.when(clientRepository.findByUserName(any(String.class)))
                .thenReturn(Optional.of(payment.getAccount().getClient()));
        Mockito.when(accountRepository.findByClientId(any(Long.class)))
                .thenReturn(Optional.of(payment.getAccount()));
        Mockito.when(paymentRepository.save(any(PaymentEntity.class)))
                .thenReturn(payment);

        OrderStatusEvent orderStatusEvent = orderCreatedEventHandler.handleEvent(orderEvent);
        log.info("Order created event: {}, hash: {}", orderEvent, orderEvent.hashCode());
        log.info("Order status event (output): {}, hash: {}", orderStatusEvent, orderStatusEvent.hashCode());
        Assertions.assertThat(orderStatusEvent).isNotNull();
        Assertions.assertThat(orderStatusEvent.getStatus()).isEqualTo(OrderStatusDto.PAYMENT_FAILED);
    }

    @Test
    public void testHandleEventWithErrorClientNotFound() {
        orderEvent.setStatus(OrderStatusDto.REGISTERED);
        payment.getAccount().setBalance(orderEvent.getAmount());
        Mockito.when(orderDataRepository.save(any(OrderDataEntity.class)))
                .thenReturn(orderData);

        OrderStatusEvent orderStatusEvent = orderCreatedEventHandler.handleEvent(orderEvent);

        log.info("Order created event: {}, hash: {}", orderEvent, orderEvent.hashCode());
        log.info("Order status event (output): {}, hash: {}", orderStatusEvent, orderStatusEvent.hashCode());
        Assertions.assertThat(orderStatusEvent).isNotNull();
        Assertions.assertThat(orderStatusEvent.getStatus()).isEqualTo(OrderStatusDto.PAYMENT_FAILED);
        Assertions.assertThat(orderStatusEvent.getStatusDescription()).isNotBlank();
        log.info("Error from order status event (output): {}", orderStatusEvent.getStatusDescription());
    }

}
