package onlinestore.deliveryservice.controller;

import lombok.extern.slf4j.Slf4j;
import onlinestore.deliveryservice.BaseIT;
import onlinestore.deliveryservice.model.entity.DeliveryEntity;
import onlinestore.deliveryservice.model.repository.DeliveryRepository;
import onlinestore.deliveryservice.service.DeliveryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@Sql(scripts = "classpath:clearAll.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DeliveryControllerIT extends BaseIT {
    @Autowired
    private DeliveryService deliveryService;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Configuration
    @ComponentScan(basePackageClasses = {DeliveryController.class})
    public static class TestConf {
    }

    @BeforeEach
    public void setUp() {
        DeliveryEntity deliveryEntity = new DeliveryEntity(1L);
        deliveryRepository.save(deliveryEntity);
    }

    @Test
    public void testListDeliveries() {
        ResponseEntity<List<DeliveryEntity>> deliveries =
            restTemplate.exchange(
                    "/delivery", HttpMethod.GET,
                    new HttpEntity<>(null, new HttpHeaders()),
                    new ParameterizedTypeReference<>() {}
            );
        log.info("response status {}, body: \n\t {}", deliveries.getStatusCode(), deliveries.getBody());
        Assertions.assertEquals(HttpStatus.OK, deliveries.getStatusCode());
        Assertions.assertNotNull(deliveries.getBody());
        Assertions.assertEquals(1, deliveries.getBody().size());
    }

    @Test
    public void testGetDelivery() {
        List<DeliveryEntity> deliveries = deliveryRepository.findAll();
        Long id = deliveries.stream().findFirst().get().getId();
        URI uri = UriComponentsBuilder.fromUriString("/delivery/{id}").build(id);
        ResponseEntity<DeliveryEntity> delivery = restTemplate.exchange(
                uri, HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                new ParameterizedTypeReference<>() {
                }
        );
        log.info("delivery (id = {}) status {}, body: \n\t {}", id, delivery.getStatusCode(), delivery.getBody());
        Assertions.assertEquals(HttpStatus.OK, delivery.getStatusCode());
        Assertions.assertNotNull(delivery.getBody());
    }

}
