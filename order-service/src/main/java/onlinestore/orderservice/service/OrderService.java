package onlinestore.orderservice.service;

import onlinestore.orderservice.dto.OrderDto;
import onlinestore.orderservice.dto.StatusDto;

import java.util.List;

public interface OrderService {

    void checkDataAndCorrectAmount(OrderDto orderDto);

    OrderDto addOrder(OrderDto orderDto);

    void updateOrderStatus(Long id, StatusDto statusDto);

    List<OrderDto> getAllOrders();

    OrderDto getOrderById(Long id);
}
