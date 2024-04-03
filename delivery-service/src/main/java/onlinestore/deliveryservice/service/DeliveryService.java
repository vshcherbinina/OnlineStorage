package onlinestore.deliveryservice.service;

import onlinestore.deliveryservice.model.entity.DeliveryEntity;
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

    public List<DeliveryEntity> findAllDeliveries() {
        List<DeliveryEntity> deliveryStatusEntities = deliveryRepository.findAll();
        deliveryStatusEntities.sort(Comparator.comparing(DeliveryEntity::getId));
        return deliveryStatusEntities;
    }

    public Optional<DeliveryEntity> getDeliveryById(Long id) {
        return deliveryRepository.findById(id);
    }
}
