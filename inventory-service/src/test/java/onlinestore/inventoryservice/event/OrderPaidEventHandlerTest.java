package onlinestore.inventoryservice.event;

import onlinestore.inventoryservice.TestUtil;
import onlinestore.inventoryservice.event.InventoryDocumentEvent;
import onlinestore.inventoryservice.event.InventoryDocumentProcessorImpl;
import onlinestore.inventoryservice.event.OrderEvent;
import onlinestore.inventoryservice.event.OrderStatusEvent;
import onlinestore.inventoryservice.event.handler.OrderPaidEventHandler;
import onlinestore.inventoryservice.model.entity.*;
import onlinestore.inventoryservice.model.repository.InventoryDetailRepository;
import onlinestore.inventoryservice.model.repository.InventoryDocumentRepository;
import onlinestore.inventoryservice.model.repository.MovementRepository;
import onlinestore.inventoryservice.model.repository.ProductRepository;
import onlinestore.inventoryservice.model.util.LockByKey;
import onlinestore.inventoryservice.model.util.RepositoryUtil;
import onlinestore.inventoryservice.service.InventoryService;
import onlinestore.inventoryservice.service.InventoryServiceImpl;
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
import reactor.core.publisher.Sinks;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@WebMvcTest(OrderPaidEventHandler.class)
public class OrderPaidEventHandlerTest {

    @Autowired
    private OrderPaidEventHandler orderPaidEventHandler;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private RepositoryUtil repositoryUtil;
    @Autowired
    private InventoryDocumentProcessorImpl inventoryDocumentProcessor;
    @MockBean
    private Sinks.Many<InventoryDocumentEvent> sink;
    @MockBean
    private InventoryDocumentRepository inventoryDocumentRepository;
    @MockBean
    private InventoryDetailRepository inventoryDetailRepository;
    @MockBean
    private MovementRepository movementRepository;
    @MockBean
    private LockByKey lockByKey;
    @MockBean
    private ProductRepository productRepository;


    @Configuration
    @ComponentScan(basePackageClasses = {InventoryDocumentProcessorImpl.class, InventoryServiceImpl.class, RepositoryUtil.class})
    public static class TestConf {
    }

    private ProductEntity product;
    private OrderEvent orderEvent;
    private InventoryDocumentEntity savedDocument;
    Logger logger = LoggerFactory.getLogger(OrderPaidEventHandler.class);

    @BeforeEach
    public void setUp() {
        orderEvent = TestUtil.getInstanceOrderEvent();
        savedDocument = TestUtil.getInstanceDocument();
        savedDocument.setId(1L);
        product = TestUtil.getProductFromDocument(savedDocument);
    }

    @Test
    public void testHandleEvent() {
        Mockito.when(inventoryDocumentRepository.save(any(InventoryDocumentEntity.class)))
                .thenReturn(savedDocument);
        Mockito.when(productRepository.findOneByArticle(any(String.class)))
                .thenReturn(Optional.of(product));
        Mockito.when(movementRepository.save(any(MovementEntity.class)))
                .thenReturn(new MovementEntity(product, 1D, -1));

        OrderStatusEvent statusEvent = orderPaidEventHandler.handleEvent(orderEvent);
        logger.info("inventory event: {}, hash: {}", statusEvent, statusEvent.hashCode());
        assertThat(statusEvent).isNotNull();
        assertThat(statusEvent.getStatus()).isEqualTo(DocumentStatus.INVENTED.toString());
        OrderStatusEvent statusEventOther = new OrderStatusEvent(statusEvent.getOrderId(), statusEvent.getDateModified(),
                statusEvent.getStatus(), statusEvent.getStatusDescription());
        assertThat(statusEvent.equals(statusEventOther)).isTrue();
    }
}
