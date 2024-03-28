package onlinestore.deliveryservice.model.repository;

import onlinestore.deliveryservice.model.entity.DeliveryStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryRepository extends JpaRepository<DeliveryStatusEntity, Long> {
}
