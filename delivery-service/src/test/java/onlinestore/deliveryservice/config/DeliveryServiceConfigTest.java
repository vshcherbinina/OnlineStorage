package onlinestore.deliveryservice.config;

import onlinestore.deliveryservice.config.DeliveryServiceConfig;
import onlinestore.deliveryservice.event.InventoryDocumentEvent;
import onlinestore.deliveryservice.event.OrderStatusEvent;
import onlinestore.deliveryservice.event.handler.EventHandler;
import onlinestore.deliveryservice.model.repository.DeliveryRepository;
import onlinestore.deliveryservice.service.DeliveryService;
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
@WebMvcTest(DeliveryServiceConfig.class)
public class DeliveryServiceConfigTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private DeliveryServiceConfig deliveryServiceConfig;
    @Autowired
    private EventHandler<InventoryDocumentEvent, OrderStatusEvent> orderInventedEventHandle;
    @MockBean
    private DeliveryService deliveryService;
    @MockBean
    private DeliveryRepository deliveryRepository;

    @Configuration
    @ComponentScan(basePackageClasses = {DeliveryServiceConfig.class, EventHandler.class})
    public static class TestConf {
    }

    @Test
    public void testInventoryDocumentProcessor() {
        Function<InventoryDocumentEvent, OrderStatusEvent> result = deliveryServiceConfig.inventoryDocumentProcessor();
        assertThat(result).isNotNull();
    }

}
