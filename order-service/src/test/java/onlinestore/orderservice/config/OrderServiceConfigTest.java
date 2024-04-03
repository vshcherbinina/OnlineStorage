package onlinestore.orderservice.config;


import onlinestore.orderservice.event.OrderProcessorImpl;
import onlinestore.orderservice.event.OrderStatusEvent;
import onlinestore.orderservice.event.consumer.EventConsumer;
import onlinestore.orderservice.model.repository.RepositoryUtil;
import onlinestore.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@WebMvcTest(OrderServiceConfig.class)
public class OrderServiceConfigTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private OrderServiceConfig orderServiceConfig;
    @Autowired
    private EventConsumer<OrderStatusEvent> orderStatusEventConsumer;
    @MockBean
    private OrderService orderService;
    @MockBean
    private RepositoryUtil repositoryUtil;

    @MockBean
    private OrderProcessorImpl orderProcessor;

    @Configuration
    @ComponentScan(basePackageClasses = {OrderServiceConfig.class, EventConsumer.class})
    public static class TestConf {
    }

    @Test
    public void testInventoryServiceConfig() {
        Consumer<OrderStatusEvent> result = orderServiceConfig.orderStatusEventProcessor();
        assertThat(result).isNotNull();
    }
}
