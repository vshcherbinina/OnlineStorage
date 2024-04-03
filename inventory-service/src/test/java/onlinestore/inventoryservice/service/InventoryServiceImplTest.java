package onlinestore.inventoryservice.service;

import onlinestore.inventoryservice.TestUtil;
import onlinestore.inventoryservice.controller.InventoryController;
import onlinestore.inventoryservice.dto.InventoryDocumentDto;
import onlinestore.inventoryservice.dto.ProductDto;
import onlinestore.inventoryservice.event.InventoryDocumentProcessorImpl;
import onlinestore.inventoryservice.model.entity.DocumentStatus;
import onlinestore.inventoryservice.model.entity.InventoryDetailEntity;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;
import onlinestore.inventoryservice.model.entity.ProductEntity;
import onlinestore.inventoryservice.model.repository.InventoryDetailRepository;
import onlinestore.inventoryservice.model.repository.InventoryDocumentRepository;
import onlinestore.inventoryservice.model.repository.MovementRepository;
import onlinestore.inventoryservice.model.repository.ProductRepository;
import onlinestore.inventoryservice.model.util.LockByKey;
import onlinestore.inventoryservice.model.util.RepositoryUtil;
import onlinestore.inventoryservice.service.InventoryService;
import onlinestore.inventoryservice.service.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@ActiveProfiles("test")
@WebMvcTest(InventoryServiceImpl.class)
public class InventoryServiceImplTest {

    @Autowired
    private RepositoryUtil repositoryUtil;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private LockByKey lockByKey;
    @MockBean
    private InventoryDocumentRepository inventoryDocumentRepository;
    @MockBean
    private InventoryDetailRepository inventoryDetailRepository;
    @MockBean
    private MovementRepository movementRepository;
    @MockBean
    private ProductRepository productRepository;

    @Configuration
    @ComponentScan(basePackageClasses = {InventoryController.class, InventoryServiceImpl.class, RepositoryUtil.class})
    public static class TestConf {
    }

    private ProductEntity product;
    private InventoryDocumentEntity document;
    private InventoryDetailEntity detail;
    private InventoryDocumentEntity savedDocument;
    private InventoryDetailEntity savedDetail;

    Logger logger = LoggerFactory.getLogger(InventoryDocumentProcessorImpl.class);

    @BeforeEach
    public void setUp() {
        document = TestUtil.getInstanceDocument();
        detail = TestUtil.getDetailFromDocument(document);
        product = TestUtil.getProductFromDocument(document);
        product.setId(1L);

        savedDocument = TestUtil.getInstanceDocument();
        savedDetail = TestUtil.getDetailFromDocument(savedDocument);
        savedDetail.setId(1L);
        savedDetail.getProduct().setId(1L);
        savedDocument.setId(1L);
    }

    @Test
    @DisplayName("Get product by article")
    public void testGetProductByArticle() {
        Mockito.when(productRepository.findOneByArticle(any(String.class)))
                .thenReturn(Optional.of(product));
        ProductEntity finedProduct = inventoryService.getProductByArticle("Cream");
        assertThat(finedProduct.getArticle()).isEqualTo(product.getArticle());
    }

    @Test
    @DisplayName("Save inventory document")
    public void testSaveDocument() {
        Mockito.when(inventoryDocumentRepository.save(any(InventoryDocumentEntity.class)))
                .thenReturn(savedDocument);
        logger.info("Document prepared: {}", document);
        inventoryService.saveDocument(document);
        logger.info("Document saved: {}", document);
        assertThat(document.getId()).isEqualTo(savedDocument.getId());
    }

    @Test
    @DisplayName("Save inventory document with details")
    public void testSaveDocumentWithDetails() {
        Mockito.when(productRepository.findOneByArticle(any(String.class)))
                .thenReturn(Optional.of(detail.getProduct()));
        Mockito.when(inventoryDocumentRepository.save(any(InventoryDocumentEntity.class)))
                .thenReturn(savedDocument);
        Mockito.when(inventoryDetailRepository.save(any(InventoryDetailEntity.class)))
                .thenReturn(savedDetail);
        logger.info("Document prepared: {}", document);
        inventoryService.saveDocumentWithDetails(document);
        logger.info("Document saved: {}", document);
        assertThat(document.getId()).isEqualTo(savedDocument.getId());
        assertThat(document.getDetails().size()).isEqualTo(savedDocument.getDetails().size());
    }

    @Test
    @DisplayName("Test inventory document hashCode, equals ..")
    public void testInventoryDocumentEntityHashCode() {
        savedDocument.setStatus(DocumentStatus.INVENTED);
        savedDocument.setDateModified(LocalDateTime.now());
        savedDocument.setStatusDescription("test");
        InventoryDocumentDto documentDto = InventoryDocumentDto.fromInventoryDocumentEntity(document);
        logger.info("DocumentEntity prepared: {}, hash: {}", document, document.hashCode());
        logger.info("DocumentDto saved: {}, hash: {}", documentDto, documentDto.hashCode());
        ProductDto productDto = ProductDto.fromProductEntity(product);
        logger.info("ProductDto: {}, hash: {}", productDto, productDto.hashCode());
        logger.info("ProductEntity: {}, hash: {}", product, product.hashCode());
        assertThat(savedDocument.getStatus()).isEqualTo(DocumentStatus.INVENTED);
        assertThat(document.equals(savedDocument)).isFalse();
        assertThat(document.hashCode()).isNotEqualTo(savedDocument.hashCode());
    }

}
