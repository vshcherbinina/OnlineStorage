package onlinestore.paymentservice.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import onlinestore.paymentservice.model.entity.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    Optional<ClientEntity> findByUserName(String userName);

    @Query(value = "select name from client where user_name = :userName", nativeQuery = true)
    String getClientNameByUserName(String userName);
}
