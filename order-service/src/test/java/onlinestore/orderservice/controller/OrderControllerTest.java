package onlinestore.orderservice.controller;

import onlinestore.orderservice.TestUtil;
import onlinestore.orderservice.event.OrderProcessor;
import onlinestore.orderservice.model.entity.OrderEntity;
import onlinestore.orderservice.model.entity.OrderStatusHistoryEntity;
import onlinestore.orderservice.model.repository.OrderDetailRepository;
import onlinestore.orderservice.model.repository.OrderRepository;
import onlinestore.orderservice.model.repository.OrderStatusHistoryRepository;
import onlinestore.orderservice.model.repository.RepositoryUtil;
import onlinestore.orderservice.service.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private RepositoryUtil repositoryUtil;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderDetailRepository orderDetailRepository;
    @MockBean
    private OrderStatusHistoryRepository orderStatusHistoryRepository;
    @MockBean
    private OrderProcessor orderProcessor;


    @Configuration
    @ComponentScan(basePackageClasses = {OrderController.class, OrderServiceImpl.class, RepositoryUtil.class})
    public static class TestConf {
    }

    private OrderEntity orderPetrov;
    private List<OrderEntity> orders;
    private List<OrderStatusHistoryEntity> historyList;

    @BeforeEach
    public void setUp() {
        orderPetrov = TestUtil.getInstanceOrderEntity();
        orderPetrov.setId(1L);
        orders = Collections.singletonList(orderPetrov);
        historyList = Collections.singletonList(new OrderStatusHistoryEntity(orderPetrov));
    }

    @Test
    @DisplayName("List all orders")
    public void testListOrders() throws Exception {
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        Mockito.when(orderStatusHistoryRepository.findAllByOrderId(any(Long.class))).thenReturn(historyList);
        mvc.perform(get("/order"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(orderPetrov.getDescription())));
    }

    @Test
    @DisplayName("Get an order by id")
    public void testGetOrderById() throws Exception {
        Mockito.when(orderRepository.findById(any(Long.class))).thenReturn(Optional.of(orderPetrov));
        Mockito.when(orderStatusHistoryRepository.findAllByOrderId(any(Long.class))).thenReturn(historyList);
        URI uri = UriComponentsBuilder.fromUriString("/order/{id}").build(1);
        mvc.perform(get(uri))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(orderPetrov.getDescription())));
    }

    @Test
    @DisplayName("Add order")
    public void testAddOrder() throws Exception {
        Mockito.when(orderRepository.save(any(OrderEntity.class))).thenReturn(orderPetrov);
        Mockito.when(orderStatusHistoryRepository.findAllByOrderId(any(Long.class))).thenReturn(historyList);
        mvc.perform(
                post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"userName\": \"first\",\n" +
                                "    \"amount\": 100,\n" +
                                "    \"destinationAddress\": \"Saint Petersburg, Nevsky Avenue, 1\",\n" +
                                "    \"description\": \"test\",\n" +
                                "    \"details\": [\n" +
                                "        {\n" +
                                "            \"productArticle\": \"Cream\",\n" +
                                "            \"price\": 50.00,\n" +
                                "            \"quantity\": 2.0,\n" +
                                "            \"amount\": 100.00\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(orderPetrov.getAmount().toString())));
    }

    @Test
    @DisplayName("Update order status, with error 'order not found'")
    public void testUpdateOrderStatusNotFound() throws Exception {
        URI uri = UriComponentsBuilder.fromUriString("/order/{id}").build(1);
        mvc.perform(
                    patch(uri)
                            .accept(MediaType.APPLICATION_JSON)
                            .content("{\"status\": \"PAID\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotModified())
                .andExpect(content().string(containsString("not found")));
    }

    @Test
    @DisplayName("Update order status")
    public void testUpdateOrderStatus() throws Exception {
        Mockito.when(orderRepository.findById(any(Long.class))).thenReturn(Optional.of(orderPetrov));
        URI uri = UriComponentsBuilder.fromUriString("/order/{id}").build(1);
        mvc.perform(
                        patch(uri)
                                .accept(MediaType.APPLICATION_JSON)
                                .content("{\"status\": \"PAID\"}")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }
}
