package onlinestore.paymentservice.event;

import lombok.extern.slf4j.Slf4j;
import onlinestore.paymentservice.TestUtil;
import onlinestore.paymentservice.config.JdbcConfig;
import onlinestore.paymentservice.dto.OrderStatusDto;
import onlinestore.paymentservice.event.handler.PaymentEventHandler;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(PaymentEventHandler.class)
public class PaymentEventHandlerTest {

    @Autowired
    private PaymentEventHandler paymentEventHandler;
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
    @MockBean
    private RestTemplate restTemplateMock;

    @Configuration
    @ComponentScan(basePackageClasses = {PaymentEventHandler.class, ClientServiceImpl.class, RepositoryUtil.class, JdbcConfig.class})
    public static class TestConf {
    }

    private OrderStatusEvent orderStatusEvent;
    private OrderDataEntity orderData;
    private PaymentEntity payment;


    @BeforeEach
    public void setUp() {
        ClientEntity clientFirst = TestUtil.getInstanceClientFirst();
        OrderEvent orderEvent = TestUtil.getInstanceOrderEvent(clientFirst);
        orderData = OrderDataEntity.fromOrderEvent(orderEvent);
        orderData.setId(1L);
        orderStatusEvent = TestUtil.getInstanceOrderStatusEvent();
        payment = TestUtil.getInstancePaymentEntity(clientFirst);
    }

    @Test
    public void testHandleEvent() {
        orderStatusEvent.setStatus(OrderStatusDto.PAID);
        Mockito.when(orderDataRepository.findByOrderId(any(Long.class)))
                .thenReturn(Optional.of(orderData));
        OrderEvent orderEvent = paymentEventHandler.handleEvent(orderStatusEvent);
        log.info("Order data: {}, hash: {}", orderData, orderData.hashCode());
        log.info("Order event (output): {}, hash: {}", orderEvent, orderEvent.hashCode());
        Assertions.assertThat(orderEvent).isNotNull();
        Assertions.assertThat(orderData.getDateModified()).isEqualTo(orderStatusEvent.getDateModified());
        Assertions.assertThat(orderData.getStatus()).isEqualTo(orderStatusEvent.getStatus());
        Assertions.assertThat(orderEvent.getDateModified()).isEqualTo(orderStatusEvent.getDateModified());
        Assertions.assertThat(orderEvent.getStatus()).isEqualTo(orderStatusEvent.getStatus());
    }

    @Test
    public void testHandleEventDelivered() {
        orderStatusEvent.setStatus(OrderStatusDto.DELIVERED);
        Mockito.when(orderDataRepository.findByOrderId(any(Long.class)))
                .thenReturn(Optional.of(orderData));
        OrderEvent orderEventResult = paymentEventHandler.handleEvent(orderStatusEvent);
        log.info("Order data: {}, hash: {}", orderData, orderData.hashCode());
        Assertions.assertThat(orderEventResult).isNull();
        Assertions.assertThat(orderData.getStatus()).isEqualTo(orderStatusEvent.getStatus());
    }

    @Test
    public void testHandleEventInventedFailed() {
        orderStatusEvent.setStatus(OrderStatusDto.INVENTION_FAILED);
        Mockito.when(orderDataRepository.findByOrderId(any(Long.class)))
                .thenReturn(Optional.of(orderData));
        Mockito.when(paymentRepository.findByOrderId(any(Long.class)))
                .thenReturn(Optional.of(payment));
        Mockito.when(accountRepository.findByClientId(any(Long.class)))
                .thenReturn(Optional.of(payment.getAccount()));
        Mockito.when(paymentRepository.save(any(PaymentEntity.class)))
                .thenReturn(payment);
        OrderEvent orderEventResult = paymentEventHandler.handleEvent(orderStatusEvent);
        log.info("Order data: {}, hash: {}", orderData, orderData.hashCode());
        Assertions.assertThat(orderEventResult).isNull();
        Assertions.assertThat(orderData.getStatus()).isEqualTo(orderStatusEvent.getStatus());
    }

    @Test
    public void testHandleEventWithErrorPaymentNotFound() {
        orderStatusEvent.setStatus(OrderStatusDto.INVENTION_FAILED);
        Mockito.when(orderDataRepository.findByOrderId(any(Long.class)))
                .thenReturn(Optional.of(orderData));
        OrderEvent orderEventResult = paymentEventHandler.handleEvent(orderStatusEvent);
        log.info("Order data: {}, hash: {}", orderData, orderData.hashCode());
        Assertions.assertThat(orderEventResult).isNull();
        Assertions.assertThat(orderData.getStatus()).isEqualTo(orderStatusEvent.getStatus());
    }
}
