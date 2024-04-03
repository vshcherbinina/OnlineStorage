package onlinestore.deliveryservice.event;

import onlinestore.deliveryservice.TestUtil;
import onlinestore.deliveryservice.event.handler.OrderInventedEventHandler;
import onlinestore.deliveryservice.model.entity.DeliveryStatus;
import onlinestore.deliveryservice.model.entity.DeliveryEntity;
import onlinestore.deliveryservice.model.repository.DeliveryRepository;
import onlinestore.deliveryservice.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@WebMvcTest(OrderInventedEventHandler.class)
public class OrderInventedEventHandlerTest {

    @Autowired
    private OrderInventedEventHandler orderInventedEventHandler;
    @Autowired
    private DeliveryService inventoryService;
    @MockBean
    private DeliveryRepository deliveryRepository;


    @Configuration
    @ComponentScan(basePackageClasses = {OrderInventedEventHandler.class, DeliveryService.class})
    public static class TestConf {
    }

    private InventoryDocumentEvent inventoryEvent;
    private DeliveryEntity deliveryEntity;
    Logger logger = LoggerFactory.getLogger(OrderInventedEventHandler.class);


    @BeforeEach
    public void setUp() {
        inventoryEvent = TestUtil.getInstanceInventoryDocumentEvent();
        deliveryEntity = TestUtil.getInstanceDeliveryEntity();
    }

    @Test
    public void testHandleEvent() {
        Mockito.when(deliveryRepository.save(any(DeliveryEntity.class)))
                .thenReturn(deliveryEntity);
        OrderStatusEvent statusEvent = orderInventedEventHandler.handleEvent(inventoryEvent);
        logger.info("delivery status hash {}: {}", statusEvent.hashCode(), statusEvent);
        assertThat(statusEvent).isNotNull();
        assertThat(statusEvent.getStatus()).containsAnyOf(DeliveryStatus.DELIVERED.toString(), DeliveryStatus.DELIVERY_FAILED.toString());
    }
}
