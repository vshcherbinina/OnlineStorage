package onlinestore.inventoryservice.event;

import onlinestore.inventoryservice.TestUtil;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;
import onlinestore.inventoryservice.model.util.RepositoryUtil;
import onlinestore.inventoryservice.service.InventoryService;
import onlinestore.inventoryservice.service.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Sinks;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@WebMvcTest(InventoryServiceImpl.class)
public class InventoryDocumentProcessorImplTest {

    @MockBean
    private Sinks.Many<InventoryDocumentEvent> sink;
    @Autowired
    private InventoryDocumentProcessorImpl inventoryDocumentProcessor;
    @MockBean
    private InventoryService inventoryService;
    @MockBean
    private RepositoryUtil repositoryUtil;

    @Configuration
    @ComponentScan(basePackageClasses = {InventoryDocumentProcessorImpl.class})
    public static class TestConf {
    }

    private InventoryDocumentEntity document;
    private InventoryDocumentEvent documentEvent;
    Logger logger = LoggerFactory.getLogger(InventoryDocumentProcessorImpl.class);

    @BeforeEach
    public void setUp() {
        document = TestUtil.getInstanceDocument();
        documentEvent = InventoryDocumentEvent.fromInventoryDocumentEntity(document);
    }

    @Test
    public void testProcess() {
        inventoryDocumentProcessor.process(document);
        logger.info("inventory document: {}", document);
        logger.info("inventory event: {}", documentEvent);
        assertThat(documentEvent.getClientName()).isEqualTo(document.getClientName());
        assertThat(documentEvent.getDetails().size()).isEqualTo(document.getDetails().size());
    }

}
