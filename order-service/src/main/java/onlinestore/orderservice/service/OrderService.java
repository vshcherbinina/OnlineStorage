package onlinestore.orderservice.service;

import onlinestore.orderservice.model.entity.OrderEntity;
import onlinestore.orderservice.dto.OrderDto;
import onlinestore.orderservice.dto.StatusDto;

import java.util.List;
import java.util.Optional;

public interface OrderService {

    void checkData(OrderDto orderDto);

    Optional<OrderDto> addOrder(OrderDto orderDto);

    void updateOrderStatus(Long id, StatusDto statusDto);

    List<OrderDto> getAllOrders();

    OrderDto getOrderById(Long id);
}
