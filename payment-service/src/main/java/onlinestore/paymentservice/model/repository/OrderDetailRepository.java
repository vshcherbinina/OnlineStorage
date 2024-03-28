package onlinestore.paymentservice.model.repository;

import onlinestore.paymentservice.model.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetailEntity, Long> {
    Optional<List<OrderDetailEntity>> findAllByOrderDataId(Long orderDataId);
}
