package onlinestore.inventoryservice.model.repository;

import onlinestore.inventoryservice.model.entity.InventoryDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDocumentRepository extends JpaRepository<InventoryDocumentEntity, Long> {
}
