package onlinestore.orderservice.controller;

import lombok.extern.slf4j.Slf4j;
import onlinestore.orderservice.BaseIT;
import onlinestore.orderservice.dto.OrderDto;
import onlinestore.orderservice.model.entity.OrderDetailEntity;
import onlinestore.orderservice.model.entity.OrderEntity;
import onlinestore.orderservice.model.entity.OrderStatus;
import onlinestore.orderservice.model.repository.OrderRepository;
import onlinestore.orderservice.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@Slf4j
@Sql(scripts = "classpath:clearAll.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class OrderControllerIT extends BaseIT {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    private OrderEntity orderFirst;

    @BeforeEach
    public void setUp() {
        orderFirst = new OrderEntity()
                .withUserName("petrov")
                .withAmount(BigDecimal.valueOf(50.00))
                .withDestinationAddress("Saint Petersburg, Nevsky Avenue, 1")
                .withDescription("test1");
        orderFirst.getDetails().add(
                new OrderDetailEntity().withProductArticle("Cream")
                        .withPrice(BigDecimal.valueOf(50))
                        .withQuantity(1D)
                        .withAmount(BigDecimal.valueOf(50)));
    }

    @Test
    public void testAddOrder() {
        ResponseEntity<OrderDto> response = addOrder();
        log.info("response status {} \n\t {}", response.getStatusCode(), response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getId());
        Assertions.assertEquals(OrderStatus.REGISTERED.toString(), response.getBody().getStatus());
    }

    private ResponseEntity<OrderDto> addOrder() {
        return restTemplate.exchange(
                "/order", HttpMethod.POST,
                new HttpEntity<>(OrderDto.fromOrder(orderFirst), new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }

    @Test
    public void testListOrders() {
        addOrder();
        ResponseEntity<List<OrderDto>> allOrders = getAllOrders();
        log.info("response status {}, body: \n\t {}", allOrders.getStatusCode(), allOrders.getBody());
        Assertions.assertEquals(HttpStatus.OK, allOrders.getStatusCode());
        Assertions.assertNotNull(allOrders.getBody());
        Assertions.assertEquals(1, allOrders.getBody().size());
        Long id = allOrders.getBody().stream().findFirst().orElse(null).getId();

        ResponseEntity<OrderDto> order = getOrderById(id);
        log.info("response (id = {}) status {}, body: \n\t {}", id, order.getStatusCode(), order.getBody());
        Assertions.assertEquals(HttpStatus.OK, order.getStatusCode());
        Assertions.assertNotNull(order.getBody());
    }

    private ResponseEntity<List<OrderDto>> getAllOrders() {
        return restTemplate.exchange(
                "/order", HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }

    private ResponseEntity<OrderDto> getOrderById(Long id) {
        String uriTemplate = "/order/{id}";
        URI uri = UriComponentsBuilder.fromUriString(uriTemplate).build(id);
        return restTemplate.exchange(
                uri, HttpMethod.GET,
                new HttpEntity<>(null, new HttpHeaders()),
                new ParameterizedTypeReference<>() {}
        );
    }


}
