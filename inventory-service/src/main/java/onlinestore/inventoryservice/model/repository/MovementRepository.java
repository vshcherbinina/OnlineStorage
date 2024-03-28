package onlinestore.inventoryservice.model.repository;

import onlinestore.inventoryservice.model.entity.MovementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovementRepository extends JpaRepository<MovementEntity, Long> {
    void deleteAllByDocumentId(Long documentId);
}
