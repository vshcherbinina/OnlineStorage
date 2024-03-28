package onlinestore.orderservice;

import onlinestore.orderservice.controller.OrderController;
import onlinestore.orderservice.dto.OrderDto;
import onlinestore.orderservice.dto.StatusDto;
import onlinestore.orderservice.model.entity.OrderDetailEntity;
import onlinestore.orderservice.model.entity.OrderEntity;
import onlinestore.orderservice.model.entity.OrderStatus;
import onlinestore.orderservice.model.repository.OrderRepository;
import onlinestore.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderService orderService;

    @Configuration
    @ComponentScan(basePackageClasses = {OrderController.class})
    public static class TestConf {
    }

    private OrderEntity orderFirst;
    private OrderEntity orderSecond;
    private List<OrderDto> orders;
    private StatusDto statusDto;

    @BeforeEach
    public void setUp() {
        orderFirst = new OrderEntity()
                .withUserName("petrov")
                .withAmount(110D)
                .withDestinationAddress("Saint Petersburg, Nevsky Avenue, 1")
                .withDescription("test1");
        orderFirst.getDetails().add(
                new OrderDetailEntity().withProductArticle("Cream")
                        .withPrice(50D)
                        .withQuantity(1D)
                        .withAmount(50D));

        orderSecond = new OrderEntity()
                .withUserName("petrov")
                .withAmount(200D)
                .withDestinationAddress("Saint Petersburg, Lenina st., 2")
                .withDescription("test2");
        orderSecond.getDetails().add(
                new OrderDetailEntity().withProductArticle("Cream")
                        .withPrice(50D)
                        .withQuantity(4D)
                        .withAmount(200D));

        orders = Collections.singletonList(OrderDto.fromOrder(orderFirst));

        statusDto = new StatusDto(OrderStatus.PAID);
        statusDto.setStatusDescription("test");
    }

    @Test
    public void getAllOrders() throws Exception {
        Mockito.when(orderService.getAllOrders()).thenReturn(orders);
        mvc.perform(get("/order"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(orderFirst.getDescription())));
    }

    @Test
    public void getOrder() throws Exception {
        Mockito.when(orderService.getOrderById(1L)).thenReturn(OrderDto.fromOrder(orderFirst));
        mvc.perform(get("/order/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(orderFirst.getDescription())));
    }

    @Test
    public void addOrder() throws Exception {
        OrderDto orderDto = OrderDto.fromOrder(orderSecond);
        Mockito.when(orderService.addOrder(orderDto)).thenReturn(Optional.of(orderDto));
        mvc.perform(
                post("/order")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"userName\": \"petrov\",\n" +
                                "    \"amount\": 100.0,\n" +
                                "    \"destinationAddress\": \"Saint Petersburg, Lenina st., 1\",\n" +
                                "    \"description\": \"test1\",\n" +
                                "    \"status\": \"REGISTERED\",\n" +
                                "    \"details\": [\n" +
                                "        {\n" +
                                "            \"productArticle\": \"Cream\",\n" +
                                "            \"price\": 50.0,\n" +
                                "            \"quantity\": 2.0,\n" +
                                "            \"amount\": 100.0\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(containsString(orderSecond.getUserName())));
    }

    @Test
    public void updateOrderStatus() throws Exception {
        doNothing().when(orderService).updateOrderStatus(1L, statusDto);
        mvc.perform(
                    patch("/order/1")
                            .accept(MediaType.APPLICATION_JSON)
                            .content("{\"status\": \"PAID\"}")
                            .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}
