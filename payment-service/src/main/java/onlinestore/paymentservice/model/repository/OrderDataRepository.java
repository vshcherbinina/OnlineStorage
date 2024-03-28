package onlinestore.paymentservice.model.repository;

import onlinestore.paymentservice.model.entity.OrderDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderDataRepository extends JpaRepository<OrderDataEntity, Long> {
    Optional<OrderDataEntity> findByOrderId(Long id);
}
