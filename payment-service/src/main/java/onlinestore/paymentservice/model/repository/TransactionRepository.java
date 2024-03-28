package onlinestore.paymentservice.model.repository;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import onlinestore.paymentservice.model.entity.TransactionEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    void deleteAllByPaymentId(Long paymentId);
}
