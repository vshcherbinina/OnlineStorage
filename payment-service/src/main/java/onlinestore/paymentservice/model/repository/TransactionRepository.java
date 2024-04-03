package onlinestore.paymentservice.model.repository;

import onlinestore.paymentservice.model.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    void deleteAllByPaymentId(Long paymentId);

    List<TransactionEntity> findAll();
}
