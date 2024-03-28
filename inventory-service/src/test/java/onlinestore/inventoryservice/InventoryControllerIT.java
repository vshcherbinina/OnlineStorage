package onlinestore.inventoryservice;

import onlinestore.inventoryservice.controller.InventoryController;
import onlinestore.inventoryservice.dto.ProductDto;
import onlinestore.inventoryservice.model.entity.ProductEntity;
import onlinestore.inventoryservice.model.repository.InventoryDetailRepository;
import onlinestore.inventoryservice.model.repository.InventoryDocumentRepository;
import onlinestore.inventoryservice.model.repository.ProductRepository;
import onlinestore.inventoryservice.service.InventoryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Sql(scripts = "classpath:clearAll.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class InventoryControllerIT extends BaseIT{
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private ProductRepository productRepository;

    Logger logger = LoggerFactory.getLogger(InventoryControllerIT.class);

    @Configuration
    @ComponentScan(basePackageClasses = {InventoryController.class})
    public static class TestConf {
    }

    private ProductDto productDto;
    private ProductDto productAddQuantityDto;

    @BeforeEach
    public void setUp() {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setArticle("Cream");
        productEntity.setDescription("Test's cream");
        productEntity.setUnit("pc");

        productDto = ProductDto.fromProductEntity(productEntity);
        productDto.setQuantity(5D);

        productAddQuantityDto = ProductDto.fromProductEntity(productEntity);
        productAddQuantityDto.setQuantity(10D);

    }

    @Test
    public void checkAddProduct() throws Exception {
        ResponseEntity<ProductDto> response = addProduct();
        logger.info("response status {} \n\t {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertEquals(productDto.getQuantity(), response.getBody().getQuantity());
    }

    private ResponseEntity<ProductDto> addProduct() throws Exception {
        return restTemplate.exchange(
                "/product", HttpMethod.POST,
                new HttpEntity<>(productDto, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }

    private ResponseEntity<ProductDto> addQuantityProduct() throws Exception {
        return restTemplate.exchange(
                "/product", HttpMethod.POST,
                new HttpEntity<>(productAddQuantityDto, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }

    @Test
    public void checkAddQuantity() throws Exception {
        addProduct();
        ResponseEntity<ProductDto> response = addQuantityProduct();
        logger.info("response status {}, body: \n\t {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue((response.getBody() != null));
        Assertions.assertEquals((productDto.getQuantity() + productAddQuantityDto.getQuantity()), response.getBody().getQuantity());
    }

    @Test
    public void checkGetProducts() throws Exception {
        addProduct();
        ResponseEntity<List<ProductDto>> allClients = getAllProducts();
        logger.info("response status {}, body: \n\t {}", allClients.getStatusCode(), allClients.getBody());
        Assertions.assertEquals(HttpStatus.OK, allClients.getStatusCode());
        Assertions.assertNotNull(allClients.getBody());
        Assertions.assertEquals(1, allClients.getBody().size());
        Long id = allClients.getBody().stream().findFirst().orElse(null).getId();

        ResponseEntity<ProductDto> client = getProduct(id);
        logger.info("response (id = {}) status {}, body: \n\t {}", id, client.getStatusCode(), client.getBody());
        Assertions.assertEquals(HttpStatus.OK, client.getStatusCode());
        Assertions.assertNotNull(client.getBody());
        Assertions.assertEquals(productDto.getQuantity(), client.getBody().getQuantity());
    }

    private ResponseEntity<List<ProductDto>> getAllProducts() throws Exception {
        return restTemplate.exchange(
                "/product", HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }

    private ResponseEntity<ProductDto> getProduct(Long id) throws Exception {
        String uriTemplate = "/product/{id}";
        URI uri = UriComponentsBuilder.fromUriString(uriTemplate).build(id);
        return restTemplate.exchange(
                uri, HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }
}
