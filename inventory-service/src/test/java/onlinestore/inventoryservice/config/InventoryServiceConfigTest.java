package onlinestore.inventoryservice.config;

import onlinestore.inventoryservice.config.InventoryServiceConfig;
import onlinestore.inventoryservice.event.InventoryDocumentProcessor;
import onlinestore.inventoryservice.event.OrderEvent;
import onlinestore.inventoryservice.event.OrderStatusEvent;
import onlinestore.inventoryservice.event.handler.EventHandler;
import onlinestore.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@WebMvcTest(InventoryServiceConfig.class)
public class InventoryServiceConfigTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private InventoryServiceConfig inventoryServiceConfig;
    @Autowired
    private EventHandler<OrderEvent, OrderStatusEvent> orderPaidEventHandle;
    @MockBean
    private InventoryService inventoryService;
    @MockBean
    private InventoryDocumentProcessor inventoryDocumentProcessor;


    @Configuration
    @ComponentScan(basePackageClasses = {InventoryServiceConfig.class, EventHandler.class})
    public static class TestConf {
    }

    @Test
    public void testOrderPaidEventProcessor() {
        Function<OrderEvent, OrderStatusEvent> result = inventoryServiceConfig.orderPaidEventProcessor();
        assertThat(result).isNotNull();
    }

}
