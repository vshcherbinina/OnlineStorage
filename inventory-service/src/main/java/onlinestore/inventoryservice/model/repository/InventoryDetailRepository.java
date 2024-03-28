package onlinestore.inventoryservice.model.repository;

import onlinestore.inventoryservice.model.entity.InventoryDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryDetailRepository extends JpaRepository<InventoryDetailEntity, Long> {
    List<InventoryDetailEntity> findAllByDocumentId(Long documentId);
}
