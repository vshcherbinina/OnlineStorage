package onlinestore.orderservice;

import onlinestore.orderservice.dto.OrderDetailDto;
import onlinestore.orderservice.dto.OrderDto;
import onlinestore.orderservice.event.OrderEvent;
import onlinestore.orderservice.model.entity.OrderEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {

    public static OrderDto getInstanceOrderDto() {
        OrderDto orderDto = OrderDto.builder()
                .userName("first")
                .destinationAddress("Saint Petersburg, Nevsky Avenue, 1")
                .amount(BigDecimal.valueOf(100))
                .description("test")
                .build();
        List<OrderDetailDto> details = new ArrayList<>();
        OrderDetailDto detailDto = OrderDetailDto.builder()
                .productArticle("Cream")
                .price(BigDecimal.valueOf(100))
                .quantity(2D)
                .amount(BigDecimal.valueOf(50))
                .build();
        details.add(detailDto);
        orderDto.setDetails(details);
        return orderDto;
    }

    public static OrderEvent getInstanceOrderEvent() {
        return OrderEvent.fromOrder(getInstanceOrderEntity());
    }

    public static OrderEntity getInstanceOrderEntity() {
        return getInstanceOrderDto().toOrder();
    }

}
