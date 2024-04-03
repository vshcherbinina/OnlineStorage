package onlinestore.paymentservice.config;

import onlinestore.paymentservice.event.OrderEvent;
import onlinestore.paymentservice.event.OrderStatusEvent;
import onlinestore.paymentservice.event.handler.EventHandler;
import onlinestore.paymentservice.model.repository.*;
import onlinestore.paymentservice.model.util.RepositoryUtil;
import onlinestore.paymentservice.service.ClientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@WebMvcTest(PaymentServiceConfig.class)
public class PaymentServiceConfigTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private PaymentServiceConfig paymentServiceConfig;
    @Autowired
    private EventHandler<OrderEvent, OrderStatusEvent> orderCreatedEventHandler;
    @Autowired
    private EventHandler<OrderStatusEvent, OrderEvent> paymentEventHandler;
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
    private RestTemplate restTemplateMock;

    @Configuration
    @ComponentScan(basePackageClasses = {PaymentServiceConfig.class, EventHandler.class, RepositoryUtil.class, ClientService.class})
    public static class TestConf {
    }

    @Test
    public void testOrderCreatedEventProcessor() {
        Function<OrderEvent, OrderStatusEvent> result = paymentServiceConfig.orderCreatedEventProcessor();
        assertThat(result).isNotNull();
    }

    @Test
    public void testPaymentEventSubscriber() {
        Function<OrderStatusEvent, OrderEvent> result = paymentServiceConfig.paymentEventSubscriber();
        assertThat(result).isNotNull();
    }


}
