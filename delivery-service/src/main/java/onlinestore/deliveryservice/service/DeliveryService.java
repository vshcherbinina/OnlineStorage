package onlinestore.deliveryservice.service;

import onlinestore.deliveryservice.model.entity.DeliveryStatusEntity;
import onlinestore.deliveryservice.model.repository.DeliveryRepository;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public List<DeliveryStatusEntity> findAllDeliveries() {
        List<DeliveryStatusEntity> deliveryStatusEntities = deliveryRepository.findAll();
        deliveryStatusEntities.sort(Comparator.comparing(DeliveryStatusEntity::getId));
        return deliveryStatusEntities;
    }

    public Optional<DeliveryStatusEntity> getDeliveryById(Long id) {
        return deliveryRepository.findById(id);
    }
}
