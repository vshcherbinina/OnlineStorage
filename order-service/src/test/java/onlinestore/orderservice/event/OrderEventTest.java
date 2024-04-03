package onlinestore.orderservice.event;

import lombok.extern.slf4j.Slf4j;
import onlinestore.orderservice.TestUtil;
import onlinestore.orderservice.dto.OrderDetailDto;
import onlinestore.orderservice.dto.OrderDto;
import onlinestore.orderservice.model.entity.OrderStatus;
import onlinestore.orderservice.model.repository.RepositoryUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@WebMvcTest(OrderEvent.class)
public class OrderEventTest {
    @MockBean
    private RepositoryUtil repositoryUtil;
    @MockBean
    private OrderProcessor orderProcessor;

    private OrderEvent eventFirst;
    private OrderEvent eventSecond;

    @Configuration
    @ComponentScan(basePackageClasses = {OrderEventTest.class})
    public static class TestConf {
    }

    @BeforeEach
    public void setUp() {
        eventFirst = TestUtil.getInstanceOrderEvent();
        eventFirst.setId(1L);

        eventSecond = OrderEvent.builder().build();
        eventSecond.setId(2L);
        eventSecond.setDateCreated(LocalDateTime.now());
        eventSecond.setDateModified(eventSecond.getDateCreated());
        eventSecond.setUserName("second");
        eventSecond.setAmount(BigDecimal.valueOf(100));
        eventSecond.setDestinationAddress("Saint Petersburg, Nevsky Avenue, 2");
        eventSecond.setDescription("test");
        eventSecond.setStatus(OrderStatus.REGISTERED.toString());
        List<OrderDetailDto> details = new ArrayList<>();
        OrderDetailDto detail = OrderDetailDto.builder().build();
        detail.setProductArticle("Cream");
        detail.setPrice(BigDecimal.valueOf(100));
        detail.setQuantity(2D);
        detail.setAmount(BigDecimal.valueOf(50));
        eventSecond.setDetails(details);
    }

    @Test
    public void testOrderEventHashCode() {
        OrderDto orderDto = TestUtil.getInstanceOrderDto();
        log.info("First order event: {}, hash: {}", eventFirst, eventFirst.hashCode());
        log.info("Second order event: {}, hash: {}", eventSecond, eventSecond.hashCode());
        log.info("Order dto: {}, hash: {}", orderDto, orderDto.hashCode());
        assertThat(eventFirst.hashCode()).isNotEqualTo(eventSecond.hashCode());
        Assertions.assertThat(eventFirst.equals(eventSecond)).isFalse();
    }
}
