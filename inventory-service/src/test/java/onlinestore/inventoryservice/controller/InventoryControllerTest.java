package onlinestore.inventoryservice.controller;

import onlinestore.inventoryservice.TestUtil;
import onlinestore.inventoryservice.controller.InventoryController;
import onlinestore.inventoryservice.dto.InventoryDocumentDto;
import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;
import onlinestore.inventoryservice.model.entity.MovementEntity;
import onlinestore.inventoryservice.model.entity.ProductEntity;
import onlinestore.inventoryservice.model.repository.InventoryDetailRepository;
import onlinestore.inventoryservice.model.repository.InventoryDocumentRepository;
import onlinestore.inventoryservice.model.repository.MovementRepository;
import onlinestore.inventoryservice.model.repository.ProductRepository;
import onlinestore.inventoryservice.model.util.LockByKey;
import onlinestore.inventoryservice.model.util.RepositoryUtil;
import onlinestore.inventoryservice.service.InventoryService;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private RepositoryUtil repositoryUtil;
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
    @ComponentScan(basePackageClasses = {InventoryController.class, InventoryService.class, RepositoryUtil.class})
    public static class TestConf {
    }

    private ProductEntity product;
    private InventoryDocumentEntity document;
    private List<ProductEntity> products;
    private List<InventoryDocumentEntity> documents;
    Logger logger = LoggerFactory.getLogger(InventoryControllerTest.class);


    @BeforeEach
    public void setUp() {
        document = TestUtil.getInstanceDocument();
        documents = Collections.singletonList(document);

        product = TestUtil.getProductFromDocument(document);
        products = Collections.singletonList(product);
    }

    @Test
    @DisplayName("List all inventory documents")
    public void testListInventoryDocuments() throws Exception {
        Mockito.when(repositoryUtil.findAll()).thenReturn(documents);
        Mockito.when(repositoryUtil.findAllDetailsByDocumentId(any(Long.class))).thenReturn(document.getDetails());
        mvc.perform(get("/inventory"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(document.getClientName())));
    }

    @Test
    @DisplayName("Get inventory document by id")
    public void testGetInventoryDocument() throws Exception {
        InventoryDocumentDto documentDto = InventoryDocumentDto.fromInventoryDocumentEntity(document);
        logger.info("DocumentDto before: {}", documentDto);
        Mockito.when(repositoryUtil.findById(any(Long.class))).thenReturn(Optional.of(document));
        URI uri = UriComponentsBuilder.fromUriString("/inventory/{id}").build(1);
        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(document.getClientName())));
    }

    @Test
    @DisplayName("List all products")
    public void testListProducts() throws Exception {
        Mockito.when(repositoryUtil.getAllProducts()).thenReturn(products);
        mvc.perform(get("/product"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(product.getArticle())));
    }

    @Test
    @DisplayName("Get product by id")
    public void testGetProduct() throws Exception {
        Mockito.when(repositoryUtil.getProductById(any(Long.class))).thenReturn(Optional.of(product));
        URI uri = UriComponentsBuilder.fromUriString("/product/{id}").build(1);
        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(product.getArticle())));
    }

    @Test
    @DisplayName("Create new product or add quantity")
    public void testAddProduct() throws Exception {
        ProductEntity savedProduct = TestUtil.getInstanceProduct();
        savedProduct.setId(1L);
        MovementEntity movement = new MovementEntity(savedProduct, 5D, 1);
        Mockito.when(productRepository.findOneByArticle(any(String.class)))
                .thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(any(ProductEntity.class))).thenReturn(savedProduct);
        Mockito.when(movementRepository.save(any(MovementEntity.class)))
                .thenReturn(movement);
        mvc.perform(
                        post("/product")
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\n" +
                                        "    \"article\": \"Cream\",\n" +
                                        "    \"price\": 50,\n" +
                                        "    \"quantity\": 5,\n" +
                                        "    \"unit\": \"pc\",\n" +
                                        "    \"stockAddress\": \"Moscow, Lenina st., 1\"\n" +
                                        "} ")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(product.getArticle())));
    }

}
