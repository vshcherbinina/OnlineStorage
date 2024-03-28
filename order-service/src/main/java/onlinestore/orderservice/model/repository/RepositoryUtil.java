package onlinestore.orderservice.model.repository;

import lombok.Data;
import onlinestore.orderservice.dto.OrderDto;
import onlinestore.orderservice.exception.OrderNotFoundException;
import onlinestore.orderservice.model.entity.OrderDetailEntity;
import onlinestore.orderservice.model.entity.OrderEntity;
import onlinestore.orderservice.model.entity.OrderStatus;
import onlinestore.orderservice.model.entity.OrderStatusHistoryEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Component
public class RepositoryUtil {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryUtil.class);

    private final OrderRepository orderRepository;

    private final OrderDetailRepository orderDetailRepository;

    private final OrderStatusHistoryRepository orderStatusHistoryRepository;

    @Autowired
    public RepositoryUtil(OrderRepository orderRepository, OrderDetailRepository orderDetailRepository, OrderStatusHistoryRepository orderStatusHistoryRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.orderStatusHistoryRepository = orderStatusHistoryRepository;
    }

    public void saveOrder(OrderEntity order) {
        OrderEntity savedOrder = orderRepository.save(order);
        order.setId(savedOrder.getId());
        order.getDetails().forEach(orderDetailRepository::save);
        orderStatusHistoryRepository.save(new OrderStatusHistoryEntity(order));
    }

    public void updateOrderStatus(Long orderId, OrderStatus status, LocalDateTime dateModification, String description) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        order.setStatus(status);
        order.setDateModified(dateModification == null ? LocalDateTime.now() : dateModification);
        orderRepository.save(order);

        OrderStatusHistoryEntity statusHistory = new OrderStatusHistoryEntity(order);
        statusHistory.setStatusDescription(description);
        orderStatusHistoryRepository.save(statusHistory);
        logger.info("Update order with id={} status success: {}, {}",
                orderId, order.getStatus(), order.getDateModified());
    }

    public void loadOrderDetails(OrderEntity orderEntity) {
        List<OrderDetailEntity> details = orderDetailRepository.findAllByOrderId(orderEntity.getId());
        details.forEach(detail -> orderEntity.getDetails().add(detail));
    }

    public OrderEntity getOrderByIdWithDetails(Long id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));
        List<OrderDetailEntity> details = orderDetailRepository.findAllByOrderId(id);
        details.forEach(detail -> order.getDetails().add(detail));
        return order;
    }


}
