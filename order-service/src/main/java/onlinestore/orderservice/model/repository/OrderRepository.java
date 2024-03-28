package onlinestore.orderservice.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import onlinestore.orderservice.model.entity.OrderEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAll();
    Optional<OrderEntity> findById(Long id);

}
