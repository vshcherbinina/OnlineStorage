package onlinestore.orderservice.model.repository;

import onlinestore.orderservice.model.entity.OrderStatusHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderStatusHistoryRepository extends JpaRepository<OrderStatusHistoryEntity, Long> {
    List<OrderStatusHistoryEntity> findAllByOrderId(Long id);
}
