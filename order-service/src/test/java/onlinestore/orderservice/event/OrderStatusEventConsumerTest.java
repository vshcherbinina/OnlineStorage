package onlinestore.orderservice.event;

import lombok.extern.slf4j.Slf4j;
import onlinestore.orderservice.TestUtil;
import onlinestore.orderservice.config.JdbcConfig;
import onlinestore.orderservice.event.consumer.OrderStatusEventConsumer;
import onlinestore.orderservice.model.entity.OrderEntity;
import onlinestore.orderservice.model.entity.OrderStatus;
import onlinestore.orderservice.model.entity.OrderStatusHistoryEntity;
import onlinestore.orderservice.model.repository.OrderDetailRepository;
import onlinestore.orderservice.model.repository.OrderRepository;
import onlinestore.orderservice.model.repository.OrderStatusHistoryRepository;
import onlinestore.orderservice.model.repository.RepositoryUtil;
import onlinestore.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(OrderStatusEventConsumer.class)
public class OrderStatusEventConsumerTest {

    @Autowired
    private OrderStatusEventConsumer orderStatusEventConsumer;
    @Autowired
    private OrderService orderService;
    @Autowired
    private RepositoryUtil repositoryUtil;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderDetailRepository orderDetailRepository;
    @MockBean
    private OrderStatusHistoryRepository orderStatusHistoryRepository;
    @MockBean
    private OrderProcessor orderProcessor;
    @Autowired
    private JdbcConfig jdbcConfig;

    @Configuration
    @ComponentScan(basePackageClasses = {OrderStatusEventConsumer.class, OrderService.class, OrderRepository.class, JdbcConfig.class})
    public static class TestConf {
    }

    private OrderStatusEvent orderStatusEvent;
    private OrderEntity orderRegistered;
    private OrderEntity orderPaid;
    private OrderStatusHistoryEntity historyEntity;

    @BeforeEach
    public void setUp() {
        orderRegistered = TestUtil.getInstanceOrderEntity();
        orderPaid = TestUtil.getInstanceOrderEntity();
        orderPaid.setId(1L);
        orderStatusEvent = new OrderStatusEvent();
        orderStatusEvent.setOrderId(1L);
        orderStatusEvent.setStatus(OrderStatus.PAID);
        orderStatusEvent.setDateModified(LocalDateTime.now());
        orderStatusEvent.setStatusDescription("test");
        historyEntity = new OrderStatusHistoryEntity();
        historyEntity.setOrder(orderPaid);
        historyEntity.setStatus(orderStatusEvent.getStatus());
        historyEntity.setDateModified(orderStatusEvent.getDateModified());
        historyEntity.setStatusDescription(orderStatusEvent.getStatusDescription());
    }

    @Test
    public void testConsumeEvent() {
        Mockito.when(orderRepository.findById(any(Long.class))).thenReturn(Optional.of(orderPaid));
        orderStatusEventConsumer.consumeEvent(orderStatusEvent);
        log.info("Order before: {}, hash: {})", orderRegistered, orderRegistered.hashCode());
        log.info("Order after: {}, hash: {}", orderPaid, orderPaid.hashCode());
        log.info("history: {}, hash: {}", historyEntity, historyEntity.hashCode());
        assertThat(historyEntity.getDateModified()).isEqualTo(orderStatusEvent.getDateModified());
        assertThat(historyEntity.getStatus()).isEqualTo(orderStatusEvent.getStatus());
    }
}
